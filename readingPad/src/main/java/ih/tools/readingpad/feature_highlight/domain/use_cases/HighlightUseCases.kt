package ih.tools.readingpad.feature_highlight.domain.use_cases

data class HighlightUseCases(
    val addHighlight: AddHighlight,
    val getPageHighlights: GetPageHighlights,
    val removeHighlightById: RemoveHighlightById
)

