package ih.tools.readingpad.feature_bookmark.domain.use_cases

import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository

class UpdateBookmarkTitle(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(bookmarkId: Long, newTitle: String) {
        repository.updateBookmarkTitle(bookmarkId, newTitle)
    }

}