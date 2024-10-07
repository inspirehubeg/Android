package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val id: Int,
    val first_page_number: Int,
    val last_page_number: Int,
    val first_chapter_number: Int,
    val last_chapter_number: Int,
    val book_id: Int,
    val count: Int,
    val content: String,
    val size: Double,
    val last_updated: Int,
    val is_deleted: Boolean?
)
