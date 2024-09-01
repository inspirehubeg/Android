package ih.tools.readingpad.feature_bookmark.data.repository

import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkDao
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow

/**
 * Concrete implementation of the BookmarkRepository interface.
 * Uses a BookmarkDao to interact with the database and perform bookmark operations.
 *
 * @property dao The BookmarkDao used to access and manipulate bookmark data in the database.
 */
class BookmarkRepositoryImpl(
    private val dao: BookmarkDao
) : BookmarkRepository {

    /**
     * Inserts a new bookmark into the database using the dao.
     *
     * @param bookmark The bookmark object to insert.
     * @return The ID of the inserted bookmark.
     */
    override suspend fun insertBookmark(bookmark: Bookmark): Long {
        return dao.insertBookmark(bookmark)
    }

    /**
     * Removes a bookmark from the database by its ID using the dao.
     *
     *@param id The ID of the bookmark to remove.
     */
    override suspend fun removeBookmarkById(id: Long) {
        dao.removeBookmarkById(id)
    }

    /**
     * Updates the title of a bookmark in the database using the dao.
     *
     * @param id The ID of the bookmark to update.
     * @param newTitle The new title for the bookmark.
     */
    override suspend fun updateBookmarkTitle(id: Long, newTitle: String) {
        dao.updateBookmarkTitle(id, newTitle)
    }

    /**
     * Retrieves all bookmarks for a specific book from the database using the dao.
     *
     * @param bookId The ID of the book.
     * @return A Flow emitting a list of bookmarks for the book.
     */
    override fun getBookmarksForBook(bookId: String): Flow<List<Bookmark>> {
        return dao.getBookmarksForBook(bookId)
    }

    /**
     * Checks if a bookmark with the given ID exists in the database using the dao.
     *
     * @param id The ID of the bookmark to check.
     * @return `true` if a bookmark with the ID exists, `false` otherwise.
     */
    override suspend fun findBookmarkById(id: Long): Boolean {
        return dao.getBookmarkById(id) != null
    }
}