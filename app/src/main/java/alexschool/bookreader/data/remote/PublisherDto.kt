package alexschool.bookreader.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class PublisherDto(
    val id: Int,
    val name: String
)
