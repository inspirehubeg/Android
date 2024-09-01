package ih.tools.readingpad.feature_highlight.domain.use_cases

import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository

/**
 * Use case for removing a highlight with a given ID from the repository.
 *
 * @property repository The HighlightRepository used to interact with highlight data.
 */
class RemoveHighlightById(
    private val repository: HighlightRepository
) {
    /**
     * Removes a highlight by its ID.
     *
     * @param highlightId The ID of the highlight to remove.
     */
    suspend operator fun invoke(highlightId: Long) {
        repository.deleteHighlightById(highlightId)
    }
}