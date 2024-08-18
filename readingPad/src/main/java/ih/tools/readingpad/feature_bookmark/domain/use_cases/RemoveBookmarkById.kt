package ih.tools.readingpad.feature_bookmark.domain.use_cases

import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository

class RemoveBookmarkById (
    private val repository: BookmarkRepository
){
    suspend operator fun invoke(id: Long) {

        repository.removeBookmarkById(id)
    }
}