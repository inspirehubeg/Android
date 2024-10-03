package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val id: Int,
    val name: String,
    val isDeleted: Boolean?,
    //val description: String,
)
