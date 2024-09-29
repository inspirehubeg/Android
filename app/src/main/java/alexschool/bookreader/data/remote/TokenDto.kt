package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val id: Int,
    val first_page: Int,
    val last_page: Int,
    val first_chapter: Int,
    val last_chapter: Int,
    val book_id: Int,
    val count: Int,
    val content: String,
)
