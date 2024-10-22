package alexSchool.network.dtos

import kotlinx.serialization.Serializable


@Serializable
data class NoteDto(
    val id: Long? = 0,
    val book_id: Int,
    val chapter_number: Int,
    val page_number: Int,
    val start: Int,
    val end: Int,
    val text: String,
    val is_deleted: Boolean?
)
