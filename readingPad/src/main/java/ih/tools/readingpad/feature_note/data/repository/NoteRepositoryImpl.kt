package ih.tools.readingpad.feature_note.data.repository

import ih.tools.readingpad.feature_note.data.data_source.NoteDao
import ih.tools.readingpad.feature_note.domain.model.Note
import ih.tools.readingpad.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override suspend fun insertNote(note: Note): Long {
        val noteId = noteDao.insert(note)
        return noteId
    }

    override suspend fun deleteNoteById(id: Long) {
        noteDao.deleteNoteById(id)
    }

    override suspend fun getPageNotes(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Note>> {
        return noteDao.getPageNotes(bookId, chapterNumber, pageNumber)
    }

    override fun getNotesForBook(bookId: String): Flow<List<Note>> {
        return noteDao.getNotesForBook(bookId)
    }

    override suspend fun updateNoteText(noteId: Long, newText: String) {
        return noteDao.updateNoteText(noteId, newText)
    }
}