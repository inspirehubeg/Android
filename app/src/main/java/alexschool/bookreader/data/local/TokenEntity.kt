package alexschool.bookreader.data.local

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "tokens",
    primaryKeys = ["bookId", "id"],
    foreignKeys = [ForeignKey(
        entity = BookEntity::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TokenEntity(
    val id: Int,
    val bookId: Int,
    val firstPage: Int,
    val lastPage: Int,
    val firstChapter: Int,
    val lastChapter: Int,
    val count: Int,
    val content: String,
)
