package app.mybooks.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

private val Light = lightColorScheme(
    primary = Color(0xFF1D3A8A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDFE6F7),
    onPrimaryContainer = Color(0xFF13203A),
    background = Color(0xFFF4F6FA),
    onBackground = Color(0xFF151A23),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF151A23),
    surfaceVariant = Color(0xFFE3E8F4),
    onSurfaceVariant = Color(0xFF4F5A6E),
    outline = Color(0xFFD3D9E3),
)

private val Dark = darkColorScheme(
    primary = Color(0xFF8FADFF),
    onPrimary = Color(0xFF0C1220),
    primaryContainer = Color(0xFF222C46),
    onPrimaryContainer = Color(0xFFE8ECF4),
    background = Color(0xFF0F131B),
    onBackground = Color(0xFFE8ECF4),
    surface = Color(0xFF181D28),
    onSurface = Color(0xFFE8ECF4),
    surfaceVariant = Color(0xFF222C46),
    onSurfaceVariant = Color(0xFF98A2B6),
    outline = Color(0xFF2B3344),
)

/** Reading text is set in a serif face, like a printed book. */
val ReadFont = FontFamily.Serif

@Composable
fun MyBooksTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) Dark else Light, content = content)
}
