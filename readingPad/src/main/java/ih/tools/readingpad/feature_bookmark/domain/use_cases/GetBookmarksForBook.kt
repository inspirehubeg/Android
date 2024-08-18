package ih.tools.readingpad.feature_bookmark.domain.use_cases

import android.util.Log
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository

import kotlinx.coroutines.flow.Flow

class GetBookmarksForBook (
    private val repository: BookmarkRepository
){
    suspend operator fun invoke(bookId: String) : Flow<List<Bookmark>>{
        Log.d("GetBookmarksForBookUseCase", "GetBookmarksForBookUseCase is invoked")

        return repository.getBookmarksForBook(bookId)
    }
}