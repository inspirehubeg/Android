package ih.tools.readingpad.feature_bookmark.domain.use_cases

import ih.tools.readingpad.feature_bookmark.domain.model.BookmarkEntity
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
     * @param bookmarkEntity The bookmark object to add.
     * @return The ID of the inserted bookmark.
     */
    suspend operator fun invoke(bookmarkEntity: BookmarkEntity) : Long {
        val bookmarkId = repository.insertBookmark(bookmarkEntity)
       return bookmarkId
    }
}