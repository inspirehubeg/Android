package ih.tools.readingpad.feature_highlight.domain.use_cases

import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository
import kotlinx.coroutines.flow.Flow

class GetPageHighlights(
    private val repository: HighlightRepository
) {
    suspend operator fun invoke(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Highlight>> {
        return repository.getPageHighlights(bookId, chapterNumber, pageNumber)
    }
}