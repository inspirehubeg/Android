package alexschool.bookreader.network.model.dto

data class Token(
    val id: Int,
    val firstPage: Int,
    val lastPage: Int,
    val firstChapter: Int,
    val lastChapter: Int,
    val book_id: Int,
    val count: Int,
    val content: String,
)
