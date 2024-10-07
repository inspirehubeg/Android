package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ReadingProgressDto (
    val book_id: Int,
    val user_id: Int,
    val progress: Int,
    val last_updated: Int,
)