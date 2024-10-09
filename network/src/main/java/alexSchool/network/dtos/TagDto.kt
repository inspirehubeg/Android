package alexSchool.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val id: Int,
    val name: String,
    val is_deleted: Boolean?,
    val last_updated: Int,
)
