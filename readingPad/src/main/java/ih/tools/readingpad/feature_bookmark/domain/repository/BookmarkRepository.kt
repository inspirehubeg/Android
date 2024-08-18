package ih.tools.readingpad.feature_bookmark.domain.repository


import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    suspend fun insertBookmark(bookmark: Bookmark) : Long

   // suspend fun deleteBookmark(bookmark: Bookmark)
    suspend fun removeBookmarkById(id: Long)
    suspend fun updateBookmarkTitle(id: Long, newTitle: String)
     fun getBookmarksForBook(bookId: String): Flow<List<Bookmark>>
    suspend fun findBookmarkById(id: Long): Boolean
    // fun getBookmarksForChapter(bookId: String): Flow<List<Bookmarks>>
}