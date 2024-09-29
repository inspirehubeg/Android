package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class SetContentDto(
    val id: Int,
    val book_id: Int,
)
