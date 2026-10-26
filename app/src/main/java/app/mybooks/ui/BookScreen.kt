package app.mybooks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybooks.data.Book
import app.mybooks.data.Prefs
import app.mybooks.tts.Reader
import app.mybooks.tts.ReaderState

@Composable
fun BookScreen(book: Book, reader: Reader, prefs: Prefs, onBack: () -> Unit) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val blocks = remember(book, tab) { blocksFor(book, tab) }
    var done by remember(book.id) { mutableStateOf(prefs.done(book.id)) }
    val listState = rememberLazyListState()
    val bars = WindowInsets.systemBars.asPaddingValues()

    LaunchedEffect(tab) {
        reader.stop()
        listState.scrollToItem(0)
    }
    DisposableEffect(Unit) { onDispose { reader.stop() } }
    // Follow the voice: keep the piece being read in view.
    LaunchedEffect(reader.current) {
        val i = reader.current
        if (i >= 0) listState.animateScrollToItem((i - 1).coerceAtLeast(0))
    }

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Row(
            Modifier.fillMaxWidth().padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
            BookCover(book, Modifier.width(44.dp), showTitle = false)
            Column(Modifier.padding(start = 12.dp)) {
                Text(book.title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
                Text(
                    "${book.author} · ${book.tagline}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        ScrollableTabRow(selectedTabIndex = tab, edgePadding = 8.dp) {
            TabNames.forEachIndexed { i, name ->
                Tab(selected = tab == i, onClick = { tab = i }, text = { Text(name) })
            }
        }
        ReaderBar(reader, blocks)
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 12.dp, end = 12.dp, top = 12.dp,
                bottom = bars.calculateBottomPadding() + 32.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            itemsIndexed(blocks, key = { i, _ -> i }) { i, block ->
                BlockView(
                    block = block,
                    active = i == reader.current,
                    done = done,
                    onToggle = { index ->
                        done = if (index in done) done - index else done + index
                        prefs.setDone(book.id, done)
                    },
                )
            }
        }
    }
}

@Composable
private fun ReaderBar(reader: Reader, blocks: List<Block>) {
    var showVoices by rememberSaveable { mutableStateOf(false) }
    var speed by remember { mutableFloatStateOf(reader.rate) }
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                enabled = reader.ready,
                onClick = {
                    when (reader.state) {
                        ReaderState.Idle -> reader.play(blocks.map { it.spoken })
                        ReaderState.Playing -> reader.pause()
                        ReaderState.Paused -> reader.resume()
                    }
                },
            ) {
                val playing = reader.state == ReaderState.Playing
                Icon(if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow, contentDescription = null)
                Text(
                    when (reader.state) {
                        ReaderState.Idle -> "Listen"
                        ReaderState.Playing -> "Pause"
                        ReaderState.Paused -> "Resume"
                    },
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
            OutlinedButton(onClick = { reader.stop() }, enabled = reader.state != ReaderState.Idle) {
                Icon(Icons.Filled.Stop, contentDescription = null)
                Text("Stop", modifier = Modifier.padding(start = 6.dp))
            }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { showVoices = true }, enabled = reader.voices.isNotEmpty()) { Text("Voice") }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Speed", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Slider(
                value = speed,
                onValueChange = { speed = it },
                onValueChangeFinished = { reader.changeSpeed(speed) },
                valueRange = 0.7f..1.4f,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            )
            Text("%.2f×".format(speed), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        if (!reader.ready) {
            Text(
                "The voice is starting. If it never does, install a text-to-speech engine in Android settings.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    if (showVoices) VoiceDialog(reader) { showVoices = false }
}

@Composable
private fun VoiceDialog(reader: Reader, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Done") } },
        title = { Text("Voice") },
        text = {
            Column {
                Text(
                    "Voices come from your phone. Pick the highest quality one. To get better voices, open " +
                        "Android Settings and search for Text-to-speech output.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                androidx.compose.foundation.lazy.LazyColumn(Modifier.heightIn(max = 320.dp).padding(top = 8.dp)) {
                    items(reader.voices, key = { it.name }) { v ->
                        Row(
                            Modifier.fillMaxWidth().clickable { reader.selectVoice(v.name) }.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(selected = v.name == reader.selectedVoice, onClick = { reader.selectVoice(v.name) })
                            Text(v.label, fontSize = 14.sp)
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun BlockView(block: Block, active: Boolean, done: Set<Int>, onToggle: (Int) -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val highlight = scheme.primary.copy(alpha = 0.16f)
    val mod = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(if (active) highlight else Color.Transparent)
        .padding(horizontal = 8.dp, vertical = 6.dp)
    when (block) {
        is LeadBlock -> Text(block.text, mod, fontFamily = ReadFont, fontSize = 19.sp, lineHeight = 28.sp, color = scheme.onSurface)
        is PartBlock -> Column(mod.padding(top = 16.dp)) {
            Text(block.name, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = scheme.onSurface)
            if (block.sub.isNotBlank()) Text(block.sub, fontSize = 13.sp, color = scheme.onSurfaceVariant)
        }
        is ChapterBlock -> Row(mod.padding(top = 8.dp), verticalAlignment = Alignment.Bottom) {
            Text(block.title, Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = scheme.onSurface)
            Text(block.tag, Modifier.padding(start = 8.dp), fontSize = 12.sp, color = scheme.onSurfaceVariant)
        }
        is BulletBlock -> Row(mod) {
            Text("•", Modifier.width(18.dp), fontSize = 17.sp, color = scheme.primary)
            Text(block.text, Modifier.weight(1f), fontFamily = ReadFont, fontSize = 17.sp, lineHeight = 25.sp, color = scheme.onSurface)
        }
        is NumberedBlock -> Row(mod) {
            Text("${block.n}.", Modifier.width(30.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = scheme.primary)
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(block.head) }
                    append(" ")
                    append(block.text)
                },
                Modifier.weight(1f),
                fontFamily = ReadFont,
                fontSize = 17.sp,
                lineHeight = 25.sp,
                color = scheme.onSurface,
            )
        }
        is CheckBlock -> {
            val ticked = block.index in done
            Row(mod.clickable { onToggle(block.index) }, verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = ticked, onCheckedChange = { onToggle(block.index) })
                Text(
                    block.text,
                    Modifier.weight(1f).padding(start = 4.dp),
                    fontFamily = ReadFont,
                    fontSize = 17.sp,
                    lineHeight = 25.sp,
                    color = if (ticked) scheme.onSurfaceVariant else scheme.onSurface,
                    textDecoration = if (ticked) TextDecoration.LineThrough else TextDecoration.None,
                )
            }
        }
        is QuoteBlock -> Column(mod.padding(vertical = 6.dp)) {
            Text(
                "“${block.text}”",
                fontFamily = ReadFont,
                fontStyle = FontStyle.Italic,
                fontSize = 19.sp,
                lineHeight = 28.sp,
                color = scheme.onSurface,
            )
            Text(block.cite, Modifier.padding(top = 4.dp), fontSize = 12.sp, color = scheme.onSurfaceVariant)
        }
        is NoteBlock -> Text(
            block.text,
            mod.padding(top = 12.dp).background(scheme.primaryContainer, RoundedCornerShape(8.dp)).padding(10.dp),
            fontSize = 14.sp,
            color = scheme.onSurface,
        )
    }
}
