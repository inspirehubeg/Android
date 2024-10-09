package alexSchool.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class BookInfoDto(
    val id: Int,
    val name: String,
    val description: String,
    val summary: String,
    val pages_number: Int,
    val chapters_number: Int,
    val reading_progress: Int?,
    val publisher_id: Int,
    val subscription_id: Int,
    val release_date: String?,
    val book_rating: Double?,
    val cover: String,
    val international_num: String?,
    val language: String,
    val size: Float?,
    val is_deleted: Boolean?,
    val last_updated: Int,
)
