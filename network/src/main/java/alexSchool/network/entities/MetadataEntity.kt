package alexSchool.network.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "metadata",
    primaryKeys = ["bookId"],
    foreignKeys = [
        ForeignKey(
            entity = BookInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MetadataEntity(
    val bookId: Int,
    val encoding: String,
    val targetLinks: String,
    val index: String
)
