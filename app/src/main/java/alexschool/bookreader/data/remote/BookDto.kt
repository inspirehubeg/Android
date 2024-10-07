package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: Int,
    val name: String,
    val description: String,
    val summary: String,
    val pages_number: Int,
    val chapters_number: Int,
    val subscription_id: Int,
    val reading_progress: Int?,
    val publisher_name: String?,
    val international_num: String?,
    val language: String,
    val cover: String,
    val index: String,
    val encoding: String,
    val book_size: Float,
    val target_links: String,
    val book_rating: Double?,
    val release_date: String?,
    val is_deleted: Boolean?,
    val last_updated: Int,
)


