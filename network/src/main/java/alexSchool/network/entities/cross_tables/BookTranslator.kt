package alexSchool.network.entities.cross_tables

import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.TranslatorEntity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "BookTranslator",
    primaryKeys = ["bookId", "translatorId"],
    foreignKeys = [
        ForeignKey(
            entity = BookInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TranslatorEntity::class,
            parentColumns = ["id"],
            childColumns = ["translatorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("translatorId")]
)
data class BookTranslator(
    val bookId: Int,
    val translatorId: Int,
    val version: Int

)