package alexschool.bookreader.data.local


import alexschool.bookreader.data.dao.AuthorDao
import alexschool.bookreader.data.dao.BookDao
import alexschool.bookreader.data.dao.CategoryDao
import alexschool.bookreader.data.dao.InputDao
import alexschool.bookreader.data.dao.SetDao
import alexschool.bookreader.data.dao.SubscriptionDao
import alexschool.bookreader.data.dao.TagDao
import alexschool.bookreader.data.dao.TokenDao
import alexschool.bookreader.data.dao.TranslatorDao
import alexSchool.network.entities.AuthorEntity
import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.CategoryEntity
import alexSchool.network.entities.MetadataEntity
import alexSchool.network.entities.PublisherEntity
import alexSchool.network.entities.ReadingProgressEntity
import alexSchool.network.entities.SavedBookEntity
import alexSchool.network.entities.SetContentEntity
import alexSchool.network.entities.SetEntity
import alexSchool.network.entities.SubscriptionEntity
import alexSchool.network.entities.TagEntity
import alexSchool.network.entities.TokenEntity
import alexSchool.network.entities.TranslatorEntity
import alexSchool.network.entities.cross_tables.BookAuthor
import alexSchool.network.entities.cross_tables.BookCategory
import alexSchool.network.entities.cross_tables.BookPublisher
import alexSchool.network.entities.cross_tables.BookTag
import alexSchool.network.entities.cross_tables.BookTranslator
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        AuthorEntity::class,
        BookInfoEntity::class,
        ReadingProgressEntity::class,
        CategoryEntity::class,
        SavedBookEntity::class,
        SetEntity::class,
        SetContentEntity::class,
        TagEntity::class,
        TokenEntity::class,
        TranslatorEntity::class,
        BookCategory::class,
        BookTag::class,
        BookAuthor::class,
        BookTranslator::class,
        SubscriptionEntity::class,
        BookPublisher::class,
        PublisherEntity::class,
        MetadataEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AlexSchoolDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun authorDao(): AuthorDao
    abstract fun translatorDao(): TranslatorDao
    abstract fun setDao(): SetDao
    abstract fun bookDao(): BookDao
    abstract fun tokenDao(): TokenDao
    abstract fun tagDao(): TagDao
    abstract fun inputDao(): InputDao
    abstract fun subscriptionDao(): SubscriptionDao

}