package ih.tools.readingpad.feature_highlight.domain.use_cases

import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving all highlights associated with a specific page from the repository.
 *
 * @property repository The HighlightRepository used to interact with highlight data.
 */
class GetPageHighlights(
    private val repository: HighlightRepository
) {
    /**
     * Retrieves all highlights for a specific page.
     *
     * @param bookId The ID of the book.
     * @param chapterNumber The chapter number.
     * @param pageNumber The page number.
     * @return A Flow emitting a list of highlights for the specified page.
     */
    suspend operator fun invoke(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Highlight>> {
        return repository.getPageHighlights(bookId, chapterNumber, pageNumber)
    }
}