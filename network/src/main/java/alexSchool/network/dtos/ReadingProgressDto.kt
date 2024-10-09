package alexSchool.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ReadingProgressDto (
    val book_id: Int,
    val user_id: Int,
    val progress: Int,
    val last_updated: Int,
)