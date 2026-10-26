package app.mybooks.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.mybooks.data.Book
import app.mybooks.data.Prefs
import app.mybooks.tts.Reader

/** The shelf, and the open book on top of it. Back returns to the shelf. */
@Composable
fun AppRoot(books: List<Book>, reader: Reader, prefs: Prefs) {
    var openId by rememberSaveable { mutableStateOf<String?>(null) }
    val book = books.firstOrNull { it.id == openId }
    BackHandler(enabled = book != null) { openId = null }
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (book == null) {
            ShelfScreen(books) { openId = it.id }
        } else {
            BookScreen(book, reader, prefs) { openId = null }
        }
    }
}
