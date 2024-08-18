package ih.tools.readingpad.feature_highlight.domain.repository

import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import kotlinx.coroutines.flow.Flow

interface HighlightRepository {
    suspend fun insertHighlight(highlight: Highlight): Long
    suspend fun deleteHighlightById(id: Long)
    suspend fun getPageHighlights(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Highlight>>
}