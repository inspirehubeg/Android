package alexSchool.network.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "tokens",
    primaryKeys = ["bookId", "id"],
    foreignKeys = [ForeignKey(
        entity = BookInfoEntity::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TokenEntity(
    val id: Int,
    val bookId: Int,
    val firstPageNumber: Int,
    val lastPageNumber: Int,
    val firstChapterNumber: Int,
    val lastChapterNumber: Int,
    val count: Int,
    val content: String,
    val size: Double,
    // val version: Int,

)
