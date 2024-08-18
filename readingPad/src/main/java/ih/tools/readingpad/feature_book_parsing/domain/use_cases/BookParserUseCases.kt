package ih.tools.readingpad.feature_book_parsing.domain.use_cases

data class BookParserUseCases(
    val parseImage: ParseImage,
    val parseFont: ParseFont,
    val parseInternalLink: ParseInternalLink,
    val parseWebLink: ParseWebLink,
    val parseRegularText: ParseRegularText,
    val parseBook: ParseBook,
    val parsePageLazy: ParsePageLazy,
    val parseInternalLinkLazy: ParseInternalLinkLazy
)
