package ih.tools.readingpad.feature_note.domain.use_cases

import ih.tools.readingpad.feature_note.domain.model.Note
import ih.tools.readingpad.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetPageNotes (private val repository: NoteRepository
) {
    /**
     * Retrieves all notes for a specific page.
     *
     * @param bookId The ID of the book.
     * @param chapterNumber The chapter number.
     * @param pageNumber The page number.
     * @return A Flow emitting a list of notes for the specified page.
     */
    suspend operator fun invoke(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Note>> {
        return repository.getPageNotes(bookId, chapterNumber, pageNumber)
    }
}
