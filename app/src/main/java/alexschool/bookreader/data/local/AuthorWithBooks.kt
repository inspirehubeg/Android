package alexschool.bookreader.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class AuthorWithBooks(
    @Embedded val author: AuthorEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "authorId",
        associateBy = Junction(
            BookAuthor::class,
            parentColumn = "authorId",
            entityColumn = "bookId"
        )
    )
    val books: List<BookEntity>
)