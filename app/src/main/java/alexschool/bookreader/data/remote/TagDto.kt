package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val id: String,
    val name: String,
    val description: String,
)
