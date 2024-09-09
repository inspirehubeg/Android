package ih.tools.readingpad.feature_note.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ih.tools.readingpad.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note) : Long

    @Query("DELETE FROM note WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    @Query("SELECT * FROM note WHERE bookId = :bookId AND chapterNumber = :chapterNumber AND pageNumber = :pageNumber")
    fun getPageNotes(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE bookId = :bookId")
    fun getNotesForBook(bookId: String): Flow<List<Note>>

    @Query("UPDATE note SET text = :newText WHERE id = :id")
    suspend fun updateNoteText(id: Long, newText: String)
}