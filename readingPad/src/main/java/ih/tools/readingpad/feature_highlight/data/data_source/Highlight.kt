package ih.tools.readingpad.feature_highlight.data.data_source

data class Highlight(
    val id: Long? ,
    val bookId: Int,
    val chapterNumber: Int,
    val pageNumber: Int,
    val start: Int,
    val end: Int,
    val text: String,
    val color: Int
)
