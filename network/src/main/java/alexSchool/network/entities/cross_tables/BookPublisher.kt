package alexSchool.network.entities.cross_tables

import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.PublisherEntity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "book_publisher",
    primaryKeys = ["bookId", "publisherId"],
    foreignKeys = [
        ForeignKey(
            entity = BookInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PublisherEntity::class,
            parentColumns = ["id"],
            childColumns = ["publisherId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("publisherId")]
)
data class BookPublisher(
    val bookId: Int,
    val publisherId: Int,
    val version: Int
)
