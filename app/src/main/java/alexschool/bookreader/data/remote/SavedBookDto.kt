package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class SavedBookDto(
    val user_id: Int,
    val book_id: Int,
    val type: String,
    val last_updated: Int,
    @Serializable(with = InstantSerializer::class)
    val saved_at: Instant?
)
