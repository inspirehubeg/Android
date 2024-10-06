package ih.tools.readingpad.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")

data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val serverId: String? = null,
    val bookId: String,
    val chapterNumber: Int,
    val pageNumber: Int,
    val start: Int,
    val end: Int,
    val text: String,
    val isDeleted: Boolean = false
)