package ih.tools.readingpad.feature_bookmark.data.data_source

data class Bookmark(
    val id: Long?,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val bookmarkTitle: String,
    val start: Int,
    val end: Int
)
