package ih.tools.readingpad.feature_bookmark.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert
    suspend fun insertBookmark(bookmark: Bookmark) : Long
//    @Delete
//    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun removeBookmarkById(id: Long)

    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId")
     fun getBookmarksForBook(bookId: String): Flow<List<Bookmark>>

     @Query("UPDATE bookmarks SET bookmarkTitle = :newTitle WHERE id = :id")
     suspend fun updateBookmarkTitle(id: Long, newTitle: String)

    @Query("SELECT * FROM bookmarks WHERE id = :id")
    suspend fun getBookmarkById(id: Long): Bookmark?
}
