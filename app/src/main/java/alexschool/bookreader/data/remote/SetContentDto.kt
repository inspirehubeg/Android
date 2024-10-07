package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class SetContentDto(
    val id: Int,
    val book_id: Int,
    val set_id: Int,
    val last_updated: Int,
)
