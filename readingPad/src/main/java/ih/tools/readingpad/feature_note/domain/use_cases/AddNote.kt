package ih.tools.readingpad.feature_note.domain.use_cases

import ih.tools.readingpad.feature_note.data.data_source.NoteEntity
import ih.tools.readingpad.feature_note.domain.repository.NoteRepository

class AddNote ( private val repository: NoteRepository
) {
    /**
     * Adds a new highlight to the repository.
     *
     * @param noteEntity The highlight object to add.
     * @return The ID of the inserted highlight.
     */
    suspend operator fun invoke(noteEntity: NoteEntity): Long {
        val id = repository.insertNote(noteEntity)
        return id
    }
}