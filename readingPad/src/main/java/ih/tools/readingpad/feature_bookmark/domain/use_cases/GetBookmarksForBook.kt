package ih.tools.readingpad.feature_bookmark.domain.use_cases

import ih.tools.readingpad.feature_bookmark.data.data_source.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving all bookmarks associated with a specific book from the repository.
 *
 * @property repository The BookmarkRepository used to interact with bookmark data.
 */
class GetBookmarksForBook (
    private val repository: BookmarkRepository
){
    /**
     * Retrieves all bookmarks for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A Flow emitting a list of bookmarks for the book.
     */
     suspend operator fun invoke(bookId: String) : Flow<List<Bookmark>>{
        return repository.getBookmarksForBook(bookId)
    }
}