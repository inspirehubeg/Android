package ih.tools.readingpad.feature_bookmark.domain.use_cases

data class BookmarkUseCases(
    val addBookmark: AddBookmark,
    val getBookmarksForBook: GetBookmarksForBook,
    val removeBookmarkById: RemoveBookmarkById,
    val updateBookmarkTitle: UpdateBookmarkTitle,
    val checkBookmarkExists: CheckBookmarkExists
    // val getAllBookmarks: GetAllBookmarks
)

