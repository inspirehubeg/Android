package ih.tools.readingpad.feature_bookmark.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for interacting with the `bookmarks` table in the database.
 */
@Dao
interface BookmarkDao {

    /*** Inserts a new bookmark into the database.
     *
     * @param bookmark The bookmark object to insert.
     * @return The ID of the inserted bookmark.
     */
    @Insert
    suspend fun insertBookmark(bookmark: Bookmark): Long

    /**
     * Removes a bookmark from the database by its ID.
     *
     * @param id The ID of the bookmark to remove.
     */
    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun removeBookmarkById(id: Long)

    /**
     * Retrieves all bookmarks associated witha specific book ID.
     *
     * @param bookId The ID of the book.
     * @return A Flow emitting a list of bookmarks for the book.
     */
    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId")
    fun getBookmarksForBook(bookId: String): Flow<List<Bookmark>>

    /**
     * Updates the title of a bookmark.
     *
     * @param id The ID of the bookmark to update.
     * @param newTitle The new title for the bookmark.
     */
    @Query("UPDATE bookmarks SET bookmarkTitle = :newTitle WHERE id = :id")
    suspend fun updateBookmarkTitle(id: Long, newTitle: String)

    /**
     * Retrieves a bookmark by its ID.
     *
     * @param id The ID of the bookmark to retrieve.
     * @return The bookmark object if found, null otherwise.
     */
    @Query("SELECT * FROM bookmarks WHERE id = :id")
    suspend fun getBookmarkById(id: Long): Bookmark?
}
