package alexschool.bookreader.domain

import kotlinx.serialization.Serializable

data class Metadata(
    val bookId: Int,
    val encoding: String,
    val index: String,
   // val targetLinks: List<TargetLink>
)
