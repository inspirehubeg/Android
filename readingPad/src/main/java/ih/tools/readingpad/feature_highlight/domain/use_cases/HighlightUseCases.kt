package ih.tools.readingpad.feature_highlight.domain.use_cases

/**
 * Data class that encapsulates various use cases related to highlight management.
 * This class helps organize and provide access to different highlight operations.
 *
 * @property addHighlight Use case for adding a new highlight.
 * @property getPageHighlights Use case for retrieving highlights associated with a specific page.
 * @property removeHighlightById Use case for removing a highlight by its ID.
 */
data class HighlightUseCases(
    val addHighlight: AddHighlight,
    val getBookHighlights: GetBookHighlights,
    val getPageHighlights: GetPageHighlights,
    val removeHighlightById: RemoveHighlightById
)

