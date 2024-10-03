package alexschool.bookreader.domain

data class Token(
    val id: Int,
    val firstPageNumber: Int,
    val lastPageNumber: Int,
    val firstChapterNumber: Int,
    val lastChapterNumber: Int,
    val bookId: Int,
    val count: Int,
    val content: String,
    val size: Double
)
