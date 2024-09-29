package alexschool.bookreader.data.local

import alexschool.bookreader.data.dao.AuthorDao
import alexschool.bookreader.data.dao.BookDao
import alexschool.bookreader.data.dao.BookInfoDao
import alexschool.bookreader.data.dao.CategoryDao
import alexschool.bookreader.data.dao.TokenDao
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        AuthorEntity::class,
        BookEntity::class,
        BookIndexEntity::class,
        BookInfoEntity::class,
        BookProgressEntity::class,
        CategoryEntity::class,
        MetadataEntity::class,
        SavedBookEntity::class,
        SetEntity::class,
        SetContentEntity::class,
        TagEntity::class,
        TargetLinkEntity::class,
        TokenEntity::class,
       // EncodingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AlexSchoolDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun bookInfoDao(): BookInfoDao
    abstract fun authorDao(): AuthorDao
    abstract fun bookDao(): BookDao
    abstract fun tokenDao(): TokenDao
}