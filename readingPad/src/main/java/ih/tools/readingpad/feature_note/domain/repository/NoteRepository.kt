package ih.tools.readingpad.feature_note.domain.repository

import ih.tools.readingpad.feature_note.domain.model.Note
import ih.tools.readingpad.feature_note.domain.model.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(noteEntity: NoteEntity): Long

    suspend fun deleteNoteById(id: Long)

    suspend fun getPageNotes(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<NoteEntity>>

    suspend fun getNotesForBook(bookId: String): Flow<List<Note>>
    suspend fun updateNoteText(noteId: Long, newText: String)
}