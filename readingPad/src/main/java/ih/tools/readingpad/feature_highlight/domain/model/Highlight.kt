package ih.tools.readingpad.feature_highlight.domain.model

data class Highlight(
    val id: Long? ,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val start: Int,
    val end: Int,
    val text: String,
    val color: Int
)
