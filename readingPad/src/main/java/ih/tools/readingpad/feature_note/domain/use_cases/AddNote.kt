package ih.tools.readingpad.feature_note.domain.use_cases

import ih.tools.readingpad.feature_note.domain.model.Note
import ih.tools.readingpad.feature_note.domain.repository.NoteRepository

class AddNote ( private val repository: NoteRepository
) {
    /**
     * Adds a new highlight to the repository.
     *
     * @param note The highlight object to add.
     * @return The ID of the inserted highlight.
     */
    suspend operator fun invoke(note: Note): Long {
        val id = repository.insertNote(note)
        return id
    }
}