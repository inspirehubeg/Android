package ih.tools.readingpad.feature_bookmark.domain.use_cases

import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository

/**
 * Use case for adding a new bookmark to the repository.
 *
 * @property repository The BookmarkRepository used to interact with bookmark data.
 */
class AddBookmark (
    private val repository: BookmarkRepository
){
    /**
     * Adds a new bookmark to the repository.
     *
     * @param bookmark The bookmark object to add.
     * @return The ID of the inserted bookmark.
     */
    suspend operator fun invoke(bookmark: Bookmark) : Long {
        val bookmarkId = repository.insertBookmark(bookmark)
       return bookmarkId
    }
}