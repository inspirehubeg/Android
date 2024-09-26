package alexschool.bookreader.network.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookInfo(
    val book: Book,
    val categories: List<Category>,
    val tags: List<Tag>,
    val author_name : String,
    val author_id : Int,
)

