package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: Int,
    val name: String,
    val description: String,
    val summary: String,
    val pages_number: String,
    val chapters_number: String,
    val subscription_id: String,
    val reading_progress: Double?,
    val cover: String,
    //val index: String,
    //val encoding: String,
    // val book_size: Int,
    //val target_links: String,
    val book_rating: Double?,
    val release_date: String
)
