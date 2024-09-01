package ih.tools.readingpad.feature_bookmark.domain.use_cases

import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository

/**
 * Use case for removing a bookmark with a given ID from the repository.
 *
 * @property repository The BookmarkRepository used to interact with bookmark data.
 */
class RemoveBookmarkById (
    private val repository: BookmarkRepository
){
    /**
     * Removes a bookmark by its ID.
     *
     * @param id The ID of the bookmark to remove.
     */
    suspend operator fun invoke(id: Long) {
        repository.removeBookmarkById(id)
    }
}