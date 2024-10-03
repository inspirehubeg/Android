package alexschool.bookreader.data.remote

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class TranslatorDto(
    @PrimaryKey val id: Int,
    val name: String,
    val is_deleted: Boolean?,

    )
