package ih.tools.readingpad.feature_highlight.data.repository

import ih.tools.readingpad.feature_highlight.data.data_source.HighlightDao
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository
import kotlinx.coroutines.flow.Flow

class HighlightRepositoryImpl(
    private val highlightDao: HighlightDao
) : HighlightRepository {
    override suspend fun insertHighlight(highlight: Highlight): Long {
        val highlightId = highlightDao.insert(highlight)
        return highlightId
    }

    override suspend fun deleteHighlightById(id: Long) {
        highlightDao.deleteByIds(id)
    }

    override suspend fun getPageHighlights(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Highlight>> {
        return highlightDao.getPageHighlights(bookId, chapterNumber, pageNumber)
    }

}