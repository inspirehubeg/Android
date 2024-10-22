package alexSchool.network.domain

data class SavedBook(
    val userId: Int,
    val bookId: Int,
    val type: String,
)
