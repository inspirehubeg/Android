package alexschool.bookreader.data.domain

import java.time.Instant

data class BookInfo(
    val id: Int,
    val name: String,
    val description: String,
    val summary: String,
    val pagesNumber: Int,
    val chaptersNumber: Int,
    val readingProgress: Int?,
    val subscriptionId: Int,
    val releaseDate: String?,
    val bookRating: Double?,
    val cover: String,
    val internationalNum: String?,
    val language: String,
    val size: Float?,
    val isDeleted: Boolean?,
    val lastOpened: Instant?
)
