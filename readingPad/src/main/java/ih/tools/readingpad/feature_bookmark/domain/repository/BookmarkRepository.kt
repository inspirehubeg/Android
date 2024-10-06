package ih.tools.readingpad.feature_bookmark.domain.repository


import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.model.BookmarkEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing bookmarks.
 * Provides an abstraction layer for accessing and manipulating bookmark data.
 */
interface BookmarkRepository {

    /**
     * Inserts a new bookmark.
     *
     * @param bookmarkEntity The bookmark object to insert.
     * @return The ID of the inserted bookmark.
     */
    suspend fun insertBookmark(bookmarkEntity: BookmarkEntity): Long

    /**
     * Removes a bookmark by its ID.
     *
     * @param id The ID of the bookmark to remove.
     */
    suspend fun removeBookmarkById(id: Long)

    /**
     * Updates the title of a bookmark.
     *
     * @param id The ID of the bookmark to update.
     * @param newTitle The new title for the bookmark.
     */
    suspend fun updateBookmarkTitle(id: Long, newTitle: String)

    /**
     * Retrieves all bookmarks for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A Flow emitting a list of bookmarks for the book.
     */
    suspend fun getBookmarksForBook(bookId: String): Flow<List<Bookmark>>

    /**
     * Checks if a bookmark with the given ID exists.
     *
     * @param id The ID of the bookmark to check.
     * @return `true` if a bookmark with the ID exists, `false` otherwise.
     */
    suspend fun findBookmarkById(id: Long): Boolean
}