package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.Instant

@Entity (tableName = "books")
@TypeConverters(InstantConverter::class)
data class BookEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val cover: String,
    val description: String,
    val summary: String,
    val subscriptionId: Int,
    val pagesNumber: Int,
    val chaptersNumber: Int,
    val publisherName: String?,
    val internationalNum: String?,
    val language: String,
    val index: String,
    val encoding: String,
    val targetLinks: String,
    val readingProgress: Int?,
    val bookRating: Double?,
    val releaseDate: String?,
    val bookSize: Float,

    val lastOpened: Instant?
   // val version: Int,
)
