package ih.tools.readingpad.feature_bookmark.domain.use_cases


import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository

/**
 * Use case for checking if a bookmark with a given ID exists in the repository.
 *
 * @property repository The BookmarkRepository used to interact with bookmark data.
 */
class CheckBookmarkExists (
    private val repository: BookmarkRepository
){
    /**
     * Checks if a bookmark with the given ID exists.
     *
     * @param bookmarkId The ID of the bookmark to check.
     * @return `true` if a bookmark with the ID exists, `false` otherwise.
     */
    suspend operator fun invoke(bookmarkId: Long) : Boolean{
       return repository.findBookmarkById(bookmarkId)
    }
}