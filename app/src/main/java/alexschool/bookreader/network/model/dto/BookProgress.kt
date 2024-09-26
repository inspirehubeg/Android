package alexschool.bookreader.network.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookProgress(
    val book_id: Int,
    val user_id: Int,
    val progress: Int,
)
