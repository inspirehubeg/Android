package ih.tools.readingpad.feature_book_parsing.domain.model

import android.text.SpannableString
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_highlight.domain.model.Highlight

/**book details that is filled and used through viewModel*/

data class BookDetailsState (
    val bookId: String= "",
    val bookTitle : String = "",
    val bookAuthor: String = "",
   // val bookCategory: String = "",
    val imageUrl: String = "",
    val bookDescription : String = "",
    val numberOfChapters: Int = 0,
    val currentChapterNumber: Int = 0,
    val pageContent: String ="",
    val bookBookmarks: List<Bookmark> = emptyList(),
    val bookHighlights: List<Highlight> = emptyList(),
    val spannableContent: SpannableString = SpannableString(pageContent)
)
