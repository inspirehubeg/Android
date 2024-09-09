package ih.tools.readingpad.feature_note.domain.repository

import ih.tools.readingpad.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note: Note): Long

    suspend fun deleteNoteById(id: Long)

    suspend fun getPageNotes(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Note>>

    fun getNotesForBook(bookId: String): Flow<List<Note>>
   suspend fun updateNoteText(noteId: Long, newText: String)
}