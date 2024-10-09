package ih.tools.readingpad.feature_book_fetching.domain.book_reader

data class OldTargetLink(
    val key: String,
    val chapterNumber: Int,
    val bookId: String,
    val pageNumber: Int,
    val index: Int
)