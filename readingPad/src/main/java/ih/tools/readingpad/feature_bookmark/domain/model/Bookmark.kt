package ih.tools.readingpad.feature_bookmark.domain.model

data class Bookmark(
    val id: Long?,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val bookmarkTitle: String,
    val start: Int,
    val end: Int
)
