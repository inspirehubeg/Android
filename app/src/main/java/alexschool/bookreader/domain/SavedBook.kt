package alexschool.bookreader.domain

import kotlinx.serialization.Serializable

data class SavedBook(
    val userId: Int,
    val bookId: Int,
    val type: String,
)
