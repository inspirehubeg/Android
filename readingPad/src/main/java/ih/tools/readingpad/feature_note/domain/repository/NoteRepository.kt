package ih.tools.readingpad.feature_note.domain.repository


import ih.tools.readingpad.feature_note.data.data_source.Note
import ih.tools.readingpad.feature_note.data.data_source.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(noteEntity: NoteEntity): Long

    suspend fun deleteNoteById(id: Long)

    suspend fun getPageNotes(
        bookId: Int,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<NoteEntity>>

    suspend fun getNotesForBook(bookId: Int): Flow<List<Note>>
    suspend fun updateNoteText(noteId: Long, newText: String)
}