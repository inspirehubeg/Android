package ih.tools.readingpad.feature_bookmark.domain.use_cases

import android.util.Log
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository

class AddBookmark (
    private val repository: BookmarkRepository
){
    suspend operator fun invoke(bookmark: Bookmark) : Long {
        Log.d("addBookmarkUseCase", "addBookmarkUseCase is invoked")
        val bookmarkId = repository.insertBookmark(bookmark)
       return bookmarkId
    }
}