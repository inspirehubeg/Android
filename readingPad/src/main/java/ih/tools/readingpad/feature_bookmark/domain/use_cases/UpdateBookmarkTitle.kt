package ih.tools.readingpad.feature_bookmark.domain.use_cases

import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository

/**
 * Use case for updating the title of a bookmark with a given ID in the repository.
 *
 * @property repository The BookmarkRepository used to interact with bookmark data.
 */
class UpdateBookmarkTitle(
    private val repository: BookmarkRepository
) {
    /**
     * Updates the title of a bookmark.
     *
     * @param bookmarkId The ID of the bookmark to update.
     * @param newTitle The new title for the bookmark.
     */
    suspend operator fun invoke(bookmarkId: Long, newTitle: String) {
        repository.updateBookmarkTitle(bookmarkId, newTitle)
    }
}