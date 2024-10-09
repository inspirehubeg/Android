package alexSchool.network.entities.cross_tables

import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.CategoryEntity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "book_categories", primaryKeys = ["bookId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = BookInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class BookCategory(
    val bookId: Int,
    val categoryId: Int,
    val version: Int
)