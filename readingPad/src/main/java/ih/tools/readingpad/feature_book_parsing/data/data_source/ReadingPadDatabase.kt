package ih.tools.readingpad.feature_book_parsing.data.data_source

import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.MetadataEntity
import alexSchool.network.entities.TokenEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkDao
import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkEntity
import ih.tools.readingpad.feature_highlight.data.data_source.HighlightDao
import ih.tools.readingpad.feature_highlight.data.data_source.HighlightEntity
import ih.tools.readingpad.feature_note.data.data_source.NoteDao
import ih.tools.readingpad.feature_note.data.data_source.NoteEntity
import ih.tools.readingpad.feature_theme_color.data.data_source.ThemeColorDao
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.remote.BookContentDao

/**
 * Room database class for the ReadingPad library.
 * Defines the database entities and provides access to the Data Access Objects (DAOs).
 */
@Database(
    entities = [
        BookmarkEntity::class,
        HighlightEntity::class,
        ThemeColor::class,
        NoteEntity::class,
        MetadataEntity::class,
        BookInfoEntity::class,
        TokenEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ReadingPadDatabase : RoomDatabase() {
    /**
     * The Data Access Object for Highlight entities.
     */
    abstract val highlightDao: HighlightDao

    /**
     * The Data Access Object for Bookmark entities.
     */
    abstract val bookmarkDao: BookmarkDao

    /**
     * The Data Access Object for ThemeColor entities.
     */
    abstract val themeColorDao: ThemeColorDao

    abstract val noteDao: NoteDao

    abstract val bookContentDao: BookContentDao
}
