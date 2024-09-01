package ih.tools.readingpad.feature_highlight.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a highlighted text selection within a book.
 *
 * @property id The unique identifier for the highlight (auto-generated).
 * @property bookId The ID of the book containing the highlight.
 * @property chapterNumber The chapter number where the highlight is located.
 * @property pageNumber The page number where the highlight is located.
 * @property start The starting character index of the highlighted text.
 * @property end The ending character index of the highlighted text.
 */
@Entity(tableName = "highlights")

data class Highlight(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val start: Int,
    val end: Int
)
