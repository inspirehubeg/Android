package alexschool.bookreader.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithBooks(
    @Embedded val tag: TagEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tagId",
        associateBy = Junction(BookTag::class,parentColumn = "tagId", entityColumn = "bookId")
    )
    val books: List<BookEntity>
)

