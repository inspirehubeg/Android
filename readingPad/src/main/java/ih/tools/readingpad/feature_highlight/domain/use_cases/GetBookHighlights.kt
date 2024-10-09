package ih.tools.readingpad.feature_highlight.domain.use_cases

import ih.tools.readingpad.feature_highlight.data.data_source.Highlight
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository
import kotlinx.coroutines.flow.Flow

class GetBookHighlights (  private val repository: HighlightRepository
){
    /**
     * Retrieves all highlights for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A Flow emitting a list of highlights for the book.
     */
    suspend operator fun invoke(bookId: String) : Flow<List<Highlight>> {
        return repository.getHighlightsForBook(bookId)
    }
}