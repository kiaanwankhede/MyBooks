package app.mybooks.tts

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.mybooks.data.Prefs
import java.util.Locale

enum class ReaderState { Idle, Playing, Paused }

data class VoiceChoice(val name: String, val label: String)

/**
 * Reads text aloud with the phone's own text-to-speech engine. Nothing is sent anywhere: no API,
 * no model. The most natural voice the phone has is chosen first, and you can pick another.
 */
class Reader(context: Context, private val prefs: Prefs) {
    private val handler = Handler(Looper.getMainLooper())
    private var engine: TextToSpeech? = null
    private var installed: List<Voice> = emptyList()
    private var texts: List<String> = emptyList()
    private var generation = 0

    var ready by mutableStateOf(false); private set
    var voices by mutableStateOf<List<VoiceChoice>>(emptyList()); private set
    var selectedVoice by mutableStateOf<String?>(null); private set
    var rate by mutableFloatStateOf(prefs.rate); private set
    var state by mutableStateOf(ReaderState.Idle); private set

    /** Index of the piece being read, or -1. */
    var current by mutableIntStateOf(-1); private set

    private val listener = object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
            val (gen, index) = parse(utteranceId) ?: return
            handler.post { if (gen == generation && state == ReaderState.Playing) current = index }
        }

        override fun onDone(utteranceId: String?) {
            val (gen, index) = parse(utteranceId) ?: return
            handler.post {
                if (gen == generation && state == ReaderState.Playing && index == texts.lastIndex) finish()
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
            val (gen, _) = parse(utteranceId) ?: return
            handler.post { if (gen == generation) finish() }
        }
    }

    init {
        engine = TextToSpeech(context.applicationContext) { status ->
            handler.post {
                val e = engine ?: return@post
                if (status != TextToSpeech.SUCCESS) return@post
                e.setOnUtteranceProgressListener(listener)
                e.setSpeechRate(rate)
                loadVoices(e)
                ready = true
            }
        }
    }

    private fun loadVoices(e: TextToSpeech) {
        val device = Locale.getDefault()
        installed = runCatching { e.voices }.getOrNull().orEmpty()
            .filter { it.locale.language == "en" && !it.features.contains("notInstalled") }
            .sortedWith(
                compareByDescending<Voice> { it.quality }
                    .thenBy { it.isNetworkConnectionRequired }
                    .thenByDescending { it.locale.country == device.country }
                    .thenBy { it.name },
            )
        voices = installed.mapIndexed { i, v -> VoiceChoice(v.name, label(i, v)) }
        val chosen = installed.firstOrNull { it.name == prefs.voice } ?: installed.firstOrNull()
        if (chosen != null) {
            selectedVoice = chosen.name
            runCatching { e.setVoice(chosen) }
        }
    }

    private fun label(index: Int, v: Voice): String {
        val quality = when {
            v.quality >= Voice.QUALITY_VERY_HIGH -> "best"
            v.quality >= Voice.QUALITY_HIGH -> "high"
            v.quality >= Voice.QUALITY_NORMAL -> "normal"
            else -> "basic"
        }
        val online = if (v.isNetworkConnectionRequired) ", needs internet" else ""
        return "${v.locale.getDisplayName(Locale.getDefault())} ${index + 1} ($quality$online)"
    }

    private fun parse(id: String?): Pair<Int, Int>? {
        val parts = id?.split(":") ?: return null
        val gen = parts.getOrNull(0)?.toIntOrNull() ?: return null
        val index = parts.getOrNull(1)?.toIntOrNull() ?: return null
        return gen to index
    }

    private fun finish() {
        generation++
        state = ReaderState.Idle
        current = -1
    }

    private fun start(from: Int) {
        val e = engine ?: return
        if (texts.isEmpty()) return
        generation++
        state = ReaderState.Playing
        current = from
        for (i in from until texts.size) {
            e.speak(texts[i], if (i == from) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD, null, "$generation:$i")
        }
    }

    /** Reads [pieces] from the start. The piece being read is exposed as [current]. */
    fun play(pieces: List<String>) {
        if (!ready) return
        texts = pieces
        start(0)
    }

    fun pause() {
        if (state != ReaderState.Playing) return
        generation++
        engine?.stop()
        state = ReaderState.Paused
    }

    fun resume() {
        if (state == ReaderState.Paused) start(current.coerceAtLeast(0))
    }

    fun stop() {
        generation++
        engine?.stop()
        state = ReaderState.Idle
        current = -1
    }

    fun changeSpeed(value: Float) {
        rate = value
        prefs.rate = value
        engine?.setSpeechRate(value)
        if (state == ReaderState.Playing) start(current.coerceAtLeast(0))
    }

    fun selectVoice(name: String) {
        val v = installed.firstOrNull { it.name == name } ?: return
        selectedVoice = name
        prefs.voice = name
        runCatching { engine?.setVoice(v) }
        if (state == ReaderState.Playing) start(current.coerceAtLeast(0))
    }

    fun shutdown() {
        engine?.stop()
        engine?.shutdown()
        engine = null
    }
}
