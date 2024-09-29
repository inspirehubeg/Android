package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class SetDto(
    val id: Int,
    val name: String,
)
