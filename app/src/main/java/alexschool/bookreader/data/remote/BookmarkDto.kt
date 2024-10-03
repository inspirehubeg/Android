package alexschool.bookreader.data.remote

data class BookmarkDto(
    val id: Long ,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val bookmarkTitle: String,
    val start: Int,
    val end: Int
)
