package ih.tools.readingpad.feature_bookmark.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val bookmarkTitle: String,
    val start: Int,
    val end: Int
)
