package alexSchool.network.entities.cross_tables


import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.TagEntity
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithBooks(
    @Embedded val tag: TagEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tagId",
        associateBy = Junction(
            BookTag::class,
            parentColumn = "tagId",
            entityColumn = "bookId"
        )
    )
    val books: List<BookInfoEntity>
)

