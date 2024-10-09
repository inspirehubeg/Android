package alexSchool.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class MetadataDto(
    val book_id: Int,
    val encoding: String,
    val target_links: String,
    val index: String
)
