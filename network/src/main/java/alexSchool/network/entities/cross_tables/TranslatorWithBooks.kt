package alexSchool.network.entities.cross_tables


import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.TranslatorEntity
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TranslatorWithBooks(
    @Embedded val translator: TranslatorEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "translatorId",
        associateBy = Junction(
            BookTranslator::class,
            parentColumn = "translatorId",
            entityColumn = "bookId"
        )
    )
    val books: List<BookInfoEntity>
)
