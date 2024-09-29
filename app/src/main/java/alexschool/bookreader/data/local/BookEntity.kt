package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val cover: String,
    val description: String,
    val summary: String,
    //val authorId: Int,
    val pagesNumber: String,
    val chaptersNumber: String,
    //val index: String,
    //val encoding: String,
    // val bookSize: Int,
    //val target_links: String,
    val subscriptionId: String,
    val readingProgress: Double?,
    val bookRating: Double?,
    val releaseDate: String
)
