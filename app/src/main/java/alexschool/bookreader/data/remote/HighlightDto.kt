package alexschool.bookreader.data.remote

data class HighlightDto(
    val id: Long ,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val text: String,
    val start: Int,
    val end: Int,
    val color: Int
)
