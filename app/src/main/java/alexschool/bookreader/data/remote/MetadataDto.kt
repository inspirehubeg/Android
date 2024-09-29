package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MetadataDto(
    val book_id: Int,
    val encoding: String,
    val index: String,
//    val target_links: List<TargetLink>
)
