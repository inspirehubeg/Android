package ih.tools.readingpad.feature_bookmark.domain.use_cases


import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository

class CheckBookmarkExists (
    private val repository: BookmarkRepository
){
    suspend operator fun invoke(bookmarkId: Long) : Boolean{

       return repository.findBookmarkById(bookmarkId)
    }
}