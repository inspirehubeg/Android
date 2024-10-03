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
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        AuthorEntity::class,
        BookEntity::class,
        BookIndexEntity::class,
        BookInfoEntity::class,
        ReadingProgressEntity::class,
        CategoryEntity::class,
        MetadataEntity::class,
        SavedBookEntity::class,
        SetEntity::class,
        SetContentEntity::class,
        TagEntity::class,
        TargetLinkEntity::class,
        TokenEntity::class,
        TranslatorEntity::class,
        BookCategory::class,
        BookTag::class,
        BookAuthor::class,
        BookTranslator::class,
        SubscriptionEntity::class


       // EncodingEntity::class
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