package ih.tools.readingpad.feature_bookmark.data.repository

import alexSchool.network.NetworkModule
import ih.tools.readingpad.feature_bookmark.data.data_source.Bookmark
import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkDao
import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkEntity

import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository
import ih.tools.readingpad.mappers.toBookmark
import ih.tools.readingpad.mappers.toBookmarkEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of the BookmarkRepository interface.
 * Uses a BookmarkDao to interact with the database and perform bookmark operations.
 *
 * @property dao The BookmarkDao used to access and manipulate bookmark data in the database.
 */
class BookmarkRepositoryImpl(
    private val dao: BookmarkDao,

    ) : BookmarkRepository {
    private val apiService = NetworkModule.provideApiService()
    private val defaultDispatcher: CoroutineDispatcher = NetworkModule.provideDispatcher()

    /**
     * Inserts a new bookmark into the database using the dao.
     *
     * @param bookmarkEntity The bookmark object to insert.
     * @return The ID of the inserted bookmark.
     */
    override suspend fun insertBookmark(bookmarkEntity: BookmarkEntity): Long {
        return dao.insertBookmark(bookmarkEntity)
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
    override suspend fun getBookmarksForBook(bookId: Int): Flow<List<Bookmark>> = flow {
        val localBookmarksForBook = withContext(defaultDispatcher)
        {
            dao.getBookmarksForBook(bookId)
        }
        emit(localBookmarksForBook.map { it.toBookmark() })
        try {
            val dtoBookmarksForBook = withContext(defaultDispatcher)
            {
                apiService.getBookmarks(bookId)
            }
            val updatedBookmarksForBook = dtoBookmarksForBook.map { dtoBookmark ->
                val localBookmark = dtoBookmark.id?.let { dao.getBookmarkById(it) }
                if (dtoBookmark.is_deleted == true) {
                    if (localBookmark != null) {
                        dao.removeBookmarkById(dtoBookmark.id!!)
                    }
                } else {
                    dao.insertBookmark(dtoBookmark.toBookmarkEntity())
                }
                dtoBookmark.toBookmarkEntity().toBookmark()
            }
            emit(updatedBookmarksForBook)
        } catch (e: Exception) {
            emit(emptyList())
        }
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