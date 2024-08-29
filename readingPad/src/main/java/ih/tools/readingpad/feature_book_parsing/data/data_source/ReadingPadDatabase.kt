package ih.tools.readingpad.feature_book_parsing.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkDao
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_highlight.data.data_source.HighlightDao
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_theme_color.data.data_source.ThemeColorDao
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor

/**
 * Room database class for the ReadingPad library.
 * Defines the database entities and provides access to the Data Access Objects (DAOs).
 */
@Database(
    entities = [
        Bookmark::class,
        Highlight::class,
        ThemeColor::class
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
}
