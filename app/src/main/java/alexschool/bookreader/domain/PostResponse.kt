package alexschool.bookreader.domain

import kotlinx.serialization.Serializable



data class PostResponse(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
