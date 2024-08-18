package ih.tools.readingpad.feature_highlight.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "highlights")

data class Highlight(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val start: Int,
    val end: Int
)
