package alexSchool.network.entities.cross_tables


import alexSchool.network.entities.AuthorEntity
import alexSchool.network.entities.BookInfoEntity
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
    val books: List<BookInfoEntity>
)