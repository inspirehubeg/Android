package alexSchool.network.domain

import alexSchool.network.dtos.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class BookInfo(
    val id: Int,
    val name: String,
    val cover: String,
    val description: String,
    val summary: String,
    val subscriptionId: Int,
    val pagesNumber: Int,
    val chaptersNumber: Int,
    val readingProgress: Int?,
    val releaseDate: String?,
    val bookRating: Double?,
    val internationalNum: String?,
    val language: String,
    val size: Float?,
    val isDeleted: Boolean?,
   @Serializable(with = InstantSerializer::class)
    val lastOpened: Instant?
)
