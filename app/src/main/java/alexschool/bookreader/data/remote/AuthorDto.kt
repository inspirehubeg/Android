package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    val id: Int,
    val name: String,
    val bio: String,
    val image: String,
    val is_deleted: Boolean?,
    val last_updated: Int
)
