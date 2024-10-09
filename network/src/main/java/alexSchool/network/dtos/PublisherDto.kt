package alexSchool.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PublisherDto(
    val id: Int,
    val name: String,
    val last_updated: Int,
    val is_deleted: Boolean?
)
