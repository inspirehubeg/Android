package ih.tools.readingpad.feature_highlight.data.repository

import ih.tools.readingpad.feature_highlight.data.data_source.HighlightDao
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository
import kotlinx.coroutines.flow.Flow

/**
 * Concrete implementation of the HighlightRepository interface.
 * Uses a HighlightDao to interact with the database and perform highlight operations.
 *
 * @property highlightDao The HighlightDao used to access and manipulate highlight data in the database.
 */
class HighlightRepositoryImpl(
    private val highlightDao: HighlightDao
) : HighlightRepository {

    /**
     * Inserts a new highlight into the database using the highlightDao.
     *
     * @param highlight The highlight object to insert.
     * @return The ID of the inserted highlight.
     */
    override suspend fun insertHighlight(highlight: Highlight): Long {
        val highlightId = highlightDao.insert(highlight)
        return highlightId
    }
    /**
     * Deletes a highlight from the database by its ID using the highlightDao.*
     * @param id The ID of the highlight to delete.
     */

    override suspend fun deleteHighlightById(id: Long) {
        highlightDao.deleteByIds(id)
    }
    /**
     * Retrieves all highlights for a specific page within a book from the database using the highlightDao.
     *
     * @param bookId The ID of the book.
     * @param chapterNumber The chapter number.
     * @param pageNumber The page number.
     * @return A Flow emitting a list of highlights for the specified page.
     */
    override suspend fun getPageHighlights(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Highlight>> {
        return highlightDao.getPageHighlights(bookId, chapterNumber, pageNumber)
    }

    override fun getHighlightsForBook(bookId: String): Flow<List<Highlight>> {
        return highlightDao.getHighlightsForBook(bookId)
    }

}