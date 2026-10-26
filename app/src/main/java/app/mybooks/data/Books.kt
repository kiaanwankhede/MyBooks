package app.mybooks.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class Chapter(val title: String, val tag: String, val points: List<String>)

data class Part(val name: String, val sub: String, val chapters: List<Chapter>)

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val year: Int,
    val tagline: String,
    val oneLine: String,
    /** Cover colour, used for the drawn cover when there is no image. */
    val color: Long,
    /** File name under assets/covers/. If it is missing the app draws a cover instead. */
    val cover: String?,
    val core: List<Pair<String, String>>,
    val parts: List<Part>,
    val practices: List<String>,
    val quotes: List<Pair<String, String>>,
    val caveat: String,
)

/** Reads the book notes from assets/books.json. To add a book, add an entry there. */
object Library {
    fun load(context: Context): List<Book> {
        val text = context.assets.open("books.json").bufferedReader().use { it.readText() }
        val arr = JSONArray(text)
        return List(arr.length()) { parse(arr.getJSONObject(it)) }
    }

    private fun parse(o: JSONObject) = Book(
        id = o.getString("id"),
        title = o.getString("title"),
        author = o.getString("author"),
        year = o.optInt("year"),
        tagline = o.optString("tagline"),
        oneLine = o.optString("oneLine"),
        color = android.graphics.Color.parseColor(o.optString("color", "#1d3a8a")).toLong() and 0xFFFFFFFFL,
        cover = o.optString("cover").ifBlank { null },
        core = o.getJSONArray("core").pairs(),
        parts = o.getJSONArray("parts").let { a ->
            List(a.length()) { i ->
                val p = a.getJSONObject(i)
                Part(
                    name = p.getString("name"),
                    sub = p.optString("sub"),
                    chapters = p.getJSONArray("chapters").let { c ->
                        List(c.length()) { j ->
                            val ch = c.getJSONArray(j)
                            Chapter(ch.getString(0), ch.getString(1), ch.getJSONArray(2).strings())
                        }
                    },
                )
            }
        },
        practices = o.getJSONArray("practices").strings(),
        quotes = o.getJSONArray("quotes").pairs(),
        caveat = o.optString("caveat"),
    )

    private fun JSONArray.strings(): List<String> = List(length()) { getString(it) }

    private fun JSONArray.pairs(): List<Pair<String, String>> =
        List(length()) { i -> getJSONArray(i).let { it.getString(0) to it.getString(1) } }
}
