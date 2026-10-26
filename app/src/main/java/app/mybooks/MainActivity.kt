package app.mybooks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.mybooks.data.Library
import app.mybooks.data.Prefs
import app.mybooks.tts.Reader
import app.mybooks.ui.AppRoot
import app.mybooks.ui.MyBooksTheme

class MainActivity : ComponentActivity() {
    private lateinit var reader: Reader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = Prefs(this)
        reader = Reader(this, prefs)
        val books = Library.load(this)
        setContent {
            MyBooksTheme { AppRoot(books, reader, prefs) }
        }
    }

    override fun onStop() {
        reader.pause()
        super.onStop()
    }

    override fun onDestroy() {
        reader.shutdown()
        super.onDestroy()
    }
}
