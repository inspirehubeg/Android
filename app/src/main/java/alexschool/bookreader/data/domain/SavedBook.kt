package alexschool.bookreader.data.domain

data class SavedBook(
    val userId: Int,
    val bookId: Int,
    val type: String,
)
