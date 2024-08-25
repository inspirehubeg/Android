package ih.tools.readingpad.feature_book_parsing.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkDao
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_highlight.data.data_source.HighlightDao
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_note_color.data.data_source.ThemeColorDao
import ih.tools.readingpad.feature_note_color.domain.model.ThemeColor

@Database(
    entities = [
        Bookmark::class,
        Highlight::class,
        ThemeColor::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val highlightDao: HighlightDao
    abstract val bookmarkDao: BookmarkDao
    abstract val themeColorDao: ThemeColorDao
}
