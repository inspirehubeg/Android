package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    val updated_at: Int
)
