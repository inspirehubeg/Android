package alexSchool.network.entities

import alexSchool.network.domain.Category
import alexSchool.network.domain.Tag
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "detailedBookInfo")
data class DetailedBookInfoEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val cover: String,
    val description: String,
    val summary: String,
    val subscriptionId: Int,
    val pagesNumber: Int,
    val chaptersNumber: Int,
    val readingProgress: Int?,
    val bookRating: Double?,
    val releaseDate: String?,
    val publisherName: List<String>,
    val internationalNum: String?,
    val language: String,
    val bookSize: Float?,
    val categories: List<Category>,
    val authorsName: List<String>,
    val tags: List<Tag>,
    val translatorsName: List<String>
)
