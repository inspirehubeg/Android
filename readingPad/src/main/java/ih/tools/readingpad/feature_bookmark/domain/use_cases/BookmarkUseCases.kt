package ih.tools.readingpad.feature_bookmark.domain.use_cases


/**
 * Data class that encapsulates various use cases related to bookmark management.
 * This class helps organize and provide access to different bookmark operations.
 *
 * @property addBookmark Usecase for adding a new bookmark.
 * @property getBookmarksForBook Use case for retrieving bookmarks associated with a specific book.
 * @property removeBookmarkById Use case for removing a bookmark by its ID.
 * @property updateBookmarkTitle Use case for updating the title of a bookmark.
 * @property checkBookmarkExists Use case for checking if a bookmark with a given ID exists.
 */
data class BookmarkUseCases(
    val addBookmark: AddBookmark,
    val getBookmarksForBook: GetBookmarksForBook,
    val removeBookmarkById: RemoveBookmarkById,
    val updateBookmarkTitle: UpdateBookmarkTitle,
    val checkBookmarkExists: CheckBookmarkExists
)

