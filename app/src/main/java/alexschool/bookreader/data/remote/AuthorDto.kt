package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    val id: Int,
    val name: String,
    //val description: String,
)
