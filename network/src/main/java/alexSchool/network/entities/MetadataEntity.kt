package alexSchool.network.entities

import alexSchool.network.domain.Encoding
import alexSchool.network.domain.TargetLink
import alexSchool.network.dtos.ChapterDto
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters

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
@TypeConverters(Converters::class)
data class MetadataEntity(
    val bookId: Int,
    val encoding: Encoding,
    val targetLinks: List<TargetLink>,
    val index: List<ChapterDto>
)
