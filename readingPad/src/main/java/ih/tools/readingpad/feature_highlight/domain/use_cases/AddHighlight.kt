package ih.tools.readingpad.feature_highlight.domain.use_cases

import ih.tools.readingpad.feature_highlight.data.data_source.HighlightEntity
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository

/**
 * Use case for adding a new highlight to the repository.
 *
 * @property repository The HighlightRepository used to interact with highlight data.
 */
class AddHighlight(
    private val repository: HighlightRepository
) {
    /**
     * Adds a new highlight to the repository.
     *
     * @param highlightEntity The highlight object to add.
     * @return The ID of the inserted highlight.
     */
    suspend operator fun invoke(highlightEntity: HighlightEntity): Long {
        val id = repository.insertHighlight(highlightEntity)
        return id
    }
}
