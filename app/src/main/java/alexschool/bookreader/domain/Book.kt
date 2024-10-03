package alexschool.bookreader.domain

data class Book(
    val id: Int,
    val name: String,
    val pagesNumber: Int,
    val chaptersNumber: Int,
    val index: String,
    val encoding: String,
    val bookSize: Float,
    val targetLinks: String,
    val readingProgress: Int?,
    val tokens: List<Token>?,
)