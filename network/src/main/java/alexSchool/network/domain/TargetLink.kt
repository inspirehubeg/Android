package alexSchool.network.domain

import kotlinx.serialization.Serializable

@Serializable
data class TargetLink(
    val key: String,
    val chapterNumber: Int,
//    val bookId: Int,
    val pageNumber: Int,
    val index: Int,

)
