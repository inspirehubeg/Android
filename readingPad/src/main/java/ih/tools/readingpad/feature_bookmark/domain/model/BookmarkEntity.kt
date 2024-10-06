package ih.tools.readingpad.feature_bookmark.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a bookmark within a book.
 *
 * @property id The unique identifier for the bookmark (auto-generated).
 * @property bookId The IDof the book containing the bookmark.
 * @property chapterNumber The chapter number where the bookmark is located.
 * @property pageNumber The page number where the bookmark is located.
 * @property bookmarkTitle The title or label of the bookmark.
 * @property start The starting character index of the bookmarked text.
 * @property end The ending character index of the bookmarked text.
 */
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val serverId: String? = null,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val bookmarkTitle: String,
    val start: Int,
    val end: Int,
    val isDeleted: Boolean = false
)
