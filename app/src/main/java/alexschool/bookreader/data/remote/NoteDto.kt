package alexschool.bookreader.data.remote

data class NoteDto(
    val id: Long ,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val start: Int,
    val end: Int,
    val text: String
)
