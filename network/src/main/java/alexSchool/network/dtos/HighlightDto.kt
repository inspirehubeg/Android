package alexSchool.network.dtos

import kotlinx.serialization.Serializable


@Serializable
data class HighlightDto(
    val id: Long? = 0,
    val book_id: String,
    val chapter_number: Int,
    val page_number: Int,
    val start: Int,
    val end: Int,
    val text: String,
    val color: Int,
    val is_deleted: Boolean?
)
