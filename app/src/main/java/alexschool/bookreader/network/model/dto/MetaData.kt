package alexschool.bookreader.network.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    val bookId: Int,
    val encoding: String,
    val index: String,
    val target_links: List<TargetLink>
)
