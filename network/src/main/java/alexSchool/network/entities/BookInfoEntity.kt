package alexSchool.network.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.Instant

@Entity(tableName = "bookInfo")
@TypeConverters(InstantConverter::class)
data class BookInfoEntity(
    @PrimaryKey
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
