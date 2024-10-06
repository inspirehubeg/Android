package ih.tools.readingpad.feature_note.data.repository

import ih.tools.readingpad.feature_note.data.data_source.NoteDao
import ih.tools.readingpad.feature_note.domain.model.Note
import ih.tools.readingpad.feature_note.domain.model.NoteEntity
import ih.tools.readingpad.feature_note.domain.repository.NoteRepository
import ih.tools.readingpad.mappers.toNote
import ih.tools.readingpad.network.BookInputApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val inputApi: BookInputApi,
    private val defaultDispatcher: CoroutineDispatcher
) : NoteRepository {
    override suspend fun insertNote(noteEntity: NoteEntity): Long {
        val noteId = noteDao.insert(noteEntity)
        return noteId
    }

    override suspend fun deleteNoteById(id: Long) {
        noteDao.deleteNoteById(id)
    }

    override suspend fun getPageNotes(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<NoteEntity>> {
        return noteDao.getPageNotes(bookId, chapterNumber, pageNumber)
    }

    override suspend fun getNotesForBook(bookId: String): Flow<List<Note>> = flow {
        val localNotesForBook = withContext(defaultDispatcher) {
            noteDao.getNotesForBook(bookId)
        }
        emit(localNotesForBook.map { it.toNote() })

//        try {
//            val dtoNotesForBook = withContext(defaultDispatcher) {
//                inputApi.getNotes(bookId)
//            }
//            val updatedNotesForBook = dtoNotesForBook.map { dtoNote ->
//                val localNote = noteDao.getNoteById(dtoNote.id)
//                if (dtoNote.is_deleted == true) {
//                    if (localNote != null) {
//                        noteDao.deleteNoteById(dtoNote.id)
//                    }
//                } else {
//                    noteDao.insert(dtoNote.toNoteEntity())
//                }
//                dtoNote.toNoteEntity().toNote()
//            }
//            emit(updatedNotesForBook)
//        } catch (e: Exception) {
//            emit(emptyList())
//        }
    }

    override suspend fun updateNoteText(noteId: Long, newText: String) {
        return noteDao.updateNoteText(noteId, newText)
    }
}