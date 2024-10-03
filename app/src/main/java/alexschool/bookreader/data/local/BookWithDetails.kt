package alexschool.bookreader.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class BookWithDetails(
    @Embedded val book: BookEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            BookCategory::class,
            parentColumn = "bookId",
            entityColumn = "categoryId"
        )
    )
    val categories: List<CategoryEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            BookTag::class,
            parentColumn = "bookId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            BookAuthor::class,
            parentColumn = "bookId",
            entityColumn = "authorId"
        )
    )
    val authors: List<AuthorEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            BookTranslator::class,
            parentColumn = "bookId",
            entityColumn = "translatorId")
    )
    val translators: List<TranslatorEntity>,

)