package ih.tools.readingpad.feature_bookmark.data.repository

import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkDao
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow

class BookmarkRepositoryImpl(
    private val dao: BookmarkDao
) : BookmarkRepository {
    override suspend fun insertBookmark(bookmark: Bookmark): Long {
        return dao.insertBookmark(bookmark)
    }
//    override suspend fun deleteBookmark(bookmark: Bookmark) {
//        dao.deleteBookmark(bookmark)
//    }

    override suspend fun removeBookmarkById(id: Long) {
        dao.removeBookmarkById(id)
    }

    override suspend fun updateBookmarkTitle(id: Long, newTitle: String) {
        dao.updateBookmarkTitle(id, newTitle)
    }

    override fun getBookmarksForBook(bookId: String): Flow<List<Bookmark>> {
        return dao.getBookmarksForBook(bookId)
    }

    override suspend fun findBookmarkById(id: Long): Boolean {
        val bookmark = dao.getBookmarkById(id)
        return bookmark != null
    }
}