package ih.tools.readingpad.feature_book_fetching.domain.book_reader

data class OldTags(
    val tagStart: String,
    val tagEnd: String,
    val tagLength: Int,
    val formatLength: Int,
    val linkKeyLength: Int,
    val webLink: String,
    val internalLink: String,
    val internalLinkTarget: String,
    val image: String,
    val pageTag: String,
    val chapterTag: String,
    val splitTag: String
)
