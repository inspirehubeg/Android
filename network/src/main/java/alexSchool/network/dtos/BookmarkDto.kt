package alexSchool.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkDto(
    val id: Long? = 0,
    val bookId: String,
    val chapter_number: Int,
    val page_number: Int,
    val bookmark_title: String,
    val start: Int,
    val end: Int,
    val is_deleted: Boolean?
)
