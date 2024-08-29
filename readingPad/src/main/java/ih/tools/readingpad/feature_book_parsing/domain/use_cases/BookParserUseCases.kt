package ih.tools.readingpad.feature_book_parsing.domain.use_cases

/**
 * Data class that encapsulates a collection of use cases for parsing different elements of a book.
 */
data class BookParserUseCases(
    /** Use case for parsing image elements within a book.*/
    val parseImage: ParseImage,
    /** Use case for parsing font style elements within a book. */
    val parseFont: ParseFont,
    /** Use case for parsing internal links within a book. */
    val parseInternalLink: ParseInternalLink,
    /** Use case for parsing web links within a book. */
    val parseWebLink: ParseWebLink,
    /** Use case for parsing regular text content within a book. */
    val parseRegularText: ParseRegularText,
    /** Use case for parsing an entire book. */
    val parseBook:ParseBook,
    /** Use case for lazily parsing a page within a book. */
    val parsePageLazy: ParsePageLazy,
    /** Use case for lazily parsing an internal link within a book. */
    val parseInternalLinkLazy: ParseInternalLinkLazy
)
