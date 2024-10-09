package ih.tools.readingpad.feature_note.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteEntity: NoteEntity): Long

    @Query("DELETE FROM note WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("SELECT * FROM note WHERE bookId = :bookId AND chapterNumber = :chapterNumber " +
            "AND pageNumber = :pageNumber AND isDeleted = 0")
    fun getPageNotes(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE bookId = :bookId AND isDeleted = 0")
    fun getNotesForBook(bookId: String): List<NoteEntity>

    @Query("UPDATE note SET text = :newText WHERE id = :id")
    suspend fun updateNoteText(id: Long, newText: String)


    @Query("SELECT * FROM note WHERE serverId = :serverId")
    suspend fun getNoteByServerId(serverId: String): NoteEntity?

    @Query("UPDATE note SET serverId = :serverId WHERE id = :localId")
    suspend fun updateServerId(localId: Long, serverId: String)

    @Query("DELETE FROM note WHERE serverId = :serverId")
    suspend fun deleteNoteByServerId(serverId: String)

    // You might also need a method to get all notes without a serverId for initial sync
    @Query("SELECT * FROM note WHERE serverId IS NULL")
    suspend fun getNotesWithoutServerId(): List<NoteEntity>

    @Query("UPDATE note SET isDeleted = 1 WHERE id = :noteId")
    suspend fun markNoteAsDeleted(noteId: Long)

    @Query("SELECT * FROM note WHERE isDeleted = 1")
    suspend fun getLocallyDeletedNotes(): List<NoteEntity>
}