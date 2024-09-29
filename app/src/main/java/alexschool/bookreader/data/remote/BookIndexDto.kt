package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class BookIndexDto(
    val book_id: Int,
    val name: String,
    val page_number: Int,
    val number: Int,
)
