package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class SavedBookDto(
    val user_id: Int,
    val book_id: Int,
    val type: String,
)
