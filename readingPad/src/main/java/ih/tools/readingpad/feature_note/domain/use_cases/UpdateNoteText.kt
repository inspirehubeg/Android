package ih.tools.readingpad.feature_note.domain.use_cases

import ih.tools.readingpad.feature_note.domain.repository.NoteRepository

/**
 * Use case for updating the title of a bookmark with a given ID in the repository.
 *
 * @property repository The BookmarkRepository used to interact with bookmark data.
 */
class UpdateNoteText(
    private val repository: NoteRepository
) {
    /**
     * Updates the title of a bookmark.
     *
     * @param noteId The ID of the bookmark to update.
     * @param newText The new title for the bookmark.
     */
     suspend operator fun invoke(noteId: Long, newText: String) {
        repository.updateNoteText(noteId, newText)
    }
}