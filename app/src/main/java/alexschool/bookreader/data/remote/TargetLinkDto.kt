package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TargetLinkDto(
    val key: String,
    val chapter_number: Int,
    val book_id: Int,
    val page_number: Int,
    val index: Int
)
