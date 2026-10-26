package app.mybooks.data

import android.content.Context

/** Small on-device settings: ticked practices, chosen voice and reading speed. */
class Prefs(context: Context) {
    private val sp = context.getSharedPreferences("mybooks", Context.MODE_PRIVATE)

    fun done(bookId: String): Set<Int> =
        sp.getStringSet("done:$bookId", emptySet()).orEmpty().mapNotNull { it.toIntOrNull() }.toSet()

    fun setDone(bookId: String, done: Set<Int>) {
        sp.edit().putStringSet("done:$bookId", done.map { it.toString() }.toSet()).apply()
    }

    var voice: String?
        get() = sp.getString("voice", null)
        set(value) { sp.edit().putString("voice", value).apply() }

    var rate: Float
        get() = sp.getFloat("rate", 1f)
        set(value) { sp.edit().putFloat("rate", value).apply() }
}
