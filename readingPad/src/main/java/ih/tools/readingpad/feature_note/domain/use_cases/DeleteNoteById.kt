package ih.tools.readingpad.feature_note.domain.use_cases

import ih.tools.readingpad.feature_note.domain.repository.NoteRepository

class DeleteNoteById (private val repository: NoteRepository
) {
    /**
     * Removes a note by its ID.
     *
     * @param noteId The ID of the note to remove.
     */
    suspend operator fun invoke(noteId: Long) {
        repository.deleteNoteById(noteId)
    }
}
