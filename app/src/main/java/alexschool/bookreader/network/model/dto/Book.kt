package alexschool.bookreader.network.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: Int,
    val name: String,
    val cover: ByteArray,
    val description: String,
    val summary: String,
    val author: Author,
    val pages_number: Int,
    val chapters_number: Int,
    //val index: String,
    //val encoding: String,
    val book_size: Int,
    //val target_links: String,
    val subscription_id: Int,
    val reading_progress: String,
    val book_rating: Double?,
    val release_date: String
)