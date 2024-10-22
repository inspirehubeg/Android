package ih.tools.readingpad.feature_book_fetching.domain.book_reader

data class OldChapter(
    val title: String,
    val chapterNumber: Int,
    val oldPages: List<OldPage>
)