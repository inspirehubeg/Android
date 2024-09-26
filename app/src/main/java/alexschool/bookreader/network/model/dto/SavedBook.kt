package alexschool.bookreader.network.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class SavedBook(
    val user_id: Int,
    val book_id: Int,
    val type: String,
)
