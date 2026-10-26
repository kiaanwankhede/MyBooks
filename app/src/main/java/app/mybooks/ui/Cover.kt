package app.mybooks.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybooks.data.Book

private val Gold = Color(0xFFFFD166)

/**
 * The book's cover. Uses the real image from assets/covers/ when it exists; otherwise draws a poster:
 * a tangle of lines that resolves into one straight line.
 */
@Composable
fun BookCover(book: Book, modifier: Modifier = Modifier, showTitle: Boolean = true) {
    val context = LocalContext.current
    val image = remember(book.cover) {
        book.cover?.let { name ->
            runCatching {
                context.assets.open("covers/$name").use { BitmapFactory.decodeStream(it) }
            }.getOrNull()?.asImageBitmap()
        }
    }
    val shape = RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 8.dp, bottomEnd = 8.dp)
    Box(
        modifier
            .aspectRatio(2f / 3f)
            .shadow(6.dp, shape)
            .clip(shape)
            .background(Color(book.color)),
    ) {
        if (image != null) {
            Image(image, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        } else {
            Canvas(Modifier.fillMaxSize()) {
                val sx = size.width / 200f
                val sy = size.height / 300f
                drawCircle(Color.White.copy(alpha = 0.07f), radius = 74f * sx, center = Offset(150f * sx, 62f * sy))
                val tangle = Path().apply {
                    moveTo(-10f * sx, 70f * sy)
                    cubicTo(30f * sx, 20f * sy, 60f * sx, 120f * sy, 90f * sx, 70f * sy)
                    cubicTo(120f * sx, 20f * sy, 130f * sx, 10f * sy, 150f * sx, 80f * sy)
                    cubicTo(165f * sx, 130f * sy, 100f * sx, 150f * sy, 70f * sx, 105f * sy)
                    cubicTo(40f * sx, 60f * sy, 20f * sx, 130f * sy, 60f * sx, 168f * sy)
                    cubicTo(100f * sx, 200f * sy, 130f * sx, 120f * sy, 160f * sx, 150f * sy)
                    cubicTo(180f * sx, 170f * sy, 190f * sx, 140f * sy, 215f * sx, 150f * sy)
                }
                drawPath(tangle, Gold, style = Stroke(width = 2.2f * sx, cap = StrokeCap.Round))
                drawLine(
                    Gold, Offset(12f * sx, 192f * sy), Offset(188f * sx, 192f * sy),
                    strokeWidth = 3f * sx, cap = StrokeCap.Round,
                )
            }
            if (showTitle) {
                Column(Modifier.align(Alignment.BottomStart).padding(start = 18.dp, end = 10.dp, bottom = 14.dp)) {
                    Text(
                        book.title.uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 21.sp,
                    )
                    Text(
                        book.author.uppercase(),
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 9.sp,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }
        // spine shading
        Box(
            Modifier
                .fillMaxHeight()
                .width(8.dp)
                .background(Brush.horizontalGradient(listOf(Color(0x47000000), Color.Transparent))),
        )
    }
}
