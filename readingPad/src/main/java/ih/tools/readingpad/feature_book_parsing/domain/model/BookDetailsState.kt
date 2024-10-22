package ih.tools.readingpad.feature_book_parsing.domain.model

import ih.tools.readingpad.feature_bookmark.data.data_source.Bookmark
import ih.tools.readingpad.feature_highlight.data.data_source.Highlight
import ih.tools.readingpad.feature_note.data.data_source.Note


/**
 * Data class representing the state of book details.
 * This class is used to hold and manage information about a book, such as its title, author, content, and associatedbookmarks and highlights.
 */
data class BookDetailsState (
    val bookId: Int= -1,
    val bookTitle : String = "",
    val bookAuthor: String = "",
    val imageUrl: String = "",
    val bookDescription : String = "",
    val numberOfChapters: Int = 0,
    val numberOfPages: Int = 0,
    val bookBookmarkEntities: List<Bookmark> = emptyList(),
    val bookNoteEntities: List<Note> = emptyList(),
    val bookHighlightEntities: List<Highlight> = emptyList(),
)
