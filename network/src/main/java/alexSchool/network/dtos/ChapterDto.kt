package alexSchool.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ChapterDto(
    val number: Int,
    val name: String,
    val pageNumber: Int
)
