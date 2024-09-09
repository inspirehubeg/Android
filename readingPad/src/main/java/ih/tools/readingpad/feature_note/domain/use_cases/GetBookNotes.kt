package ih.tools.readingpad.feature_note.domain.use_cases

import ih.tools.readingpad.feature_note.domain.model.Note
import ih.tools.readingpad.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetBookNotes(
    private val repository: NoteRepository
) {
    /**
     * Retrieves all notes for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A Flow emitting a list of notes for the book.
     */
    operator fun invoke(bookId: String): Flow<List<Note>> {
        return repository.getNotesForBook(bookId)
    }
}