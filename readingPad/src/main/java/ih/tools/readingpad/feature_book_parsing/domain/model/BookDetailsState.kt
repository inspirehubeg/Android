package ih.tools.readingpad.feature_book_parsing.domain.model

import android.text.SpannableString
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_highlight.domain.model.Highlight

/**
 * Data class representing the state of book details.
 * This class is used to hold and manage information about a book, such as its title, author, content, and associatedbookmarks and highlights.
 */
data class BookDetailsState (
    val bookId: String= "",
    val bookTitle : String = "",
    val bookAuthor: String = "",
    val imageUrl: String = "",
    val bookDescription : String = "",
    val numberOfChapters: Int = 0,
    val currentChapterNumber: Int = 0,
    val pageContent: String ="",
    val bookBookmarks: List<Bookmark> = emptyList(),
    val bookHighlights: List<Highlight> = emptyList(),
    val spannableContent: SpannableString = SpannableString(pageContent)
)
