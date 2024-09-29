package alexschool.bookreader.domain

data class Token(
    val id: Int,
    val firstPage: Int,
    val lastPage: Int,
    val firstChapter: Int,
    val lastChapter: Int,
    val bookId: Int,
    val count: Int,
    val content: String,
)
