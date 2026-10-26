package app.mybooks.ui

import app.mybooks.data.Book

/** One readable piece of a tab. Each piece is also one piece of speech, so it can be highlighted as it is read. */
sealed interface Block {
    val spoken: String
}

data class LeadBlock(val text: String) : Block { override val spoken get() = text }
data class PartBlock(val name: String, val sub: String) : Block { override val spoken get() = name }
data class ChapterBlock(val title: String, val tag: String) : Block { override val spoken get() = title }
data class BulletBlock(val text: String) : Block { override val spoken get() = text }
data class NumberedBlock(val n: Int, val head: String, val text: String) : Block { override val spoken get() = "$head $text" }
data class CheckBlock(val index: Int, val text: String) : Block { override val spoken get() = text }
data class QuoteBlock(val text: String, val cite: String) : Block { override val spoken get() = text }
data class NoteBlock(val text: String) : Block { override val spoken get() = text }

val TabNames = listOf("In one page", "Chapter notes", "Practices", "Quotes")

fun blocksFor(book: Book, tab: Int): List<Block> = when (tab) {
    0 -> buildList<Block> {
        add(LeadBlock(book.oneLine))
        book.core.forEachIndexed { i, (head, text) -> add(NumberedBlock(i + 1, head, text)) }
        if (book.caveat.isNotBlank()) add(NoteBlock(book.caveat))
    }
    1 -> buildList<Block> {
        book.parts.forEach { part ->
            add(PartBlock(part.name, part.sub))
            part.chapters.forEach { ch ->
                add(ChapterBlock(ch.title, ch.tag))
                ch.points.forEach { add(BulletBlock(it)) }
            }
        }
    }
    2 -> buildList<Block> {
        add(LeadBlock("The book is meant to be practiced. Tick these off as you try them."))
        book.practices.forEachIndexed { i, text -> add(CheckBlock(i, text)) }
    }
    else -> book.quotes.map { (q, who) -> QuoteBlock(q, who) }
}
