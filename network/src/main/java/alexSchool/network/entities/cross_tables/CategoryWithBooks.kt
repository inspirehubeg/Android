package alexSchool.network.entities.cross_tables


import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.CategoryEntity
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CategoryWithBooks(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId",
        associateBy = Junction(
            BookCategory::class,
            parentColumn = "categoryId",
            entityColumn = "bookId"
        )
    )
    val books: List<BookInfoEntity>
)
