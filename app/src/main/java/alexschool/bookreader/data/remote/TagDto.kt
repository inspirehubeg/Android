package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val id: Int,
    val name: String,
    val is_deleted: Boolean?,
    val last_updated: Int,
    //val description: String,
)
