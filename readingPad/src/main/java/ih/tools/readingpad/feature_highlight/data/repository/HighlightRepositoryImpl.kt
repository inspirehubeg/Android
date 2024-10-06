package ih.tools.readingpad.feature_highlight.data.repository

import ih.tools.readingpad.feature_highlight.data.data_source.HighlightDao
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.model.HighlightEntity
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository
import ih.tools.readingpad.mappers.toHighlight
import ih.tools.readingpad.network.BookInputApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of the HighlightRepository interface.
 * Uses a HighlightDao to interact with the database and perform highlight operations.
 *
 * @property highlightDao The HighlightDao used to access and manipulate highlight data in the database.
 */
class HighlightRepositoryImpl(
    private val highlightDao: HighlightDao,
    private val inputApi: BookInputApi,
    private val defaultDispatcher: CoroutineDispatcher
) : HighlightRepository {

    /**
     * Inserts a new highlight into the database using the highlightDao.
     *
     * @param highlightEntity The highlight object to insert.
     * @return The ID of the inserted highlight.
     */
    override suspend fun insertHighlight(highlightEntity: HighlightEntity): Long {
        val highlightId = highlightDao.insert(highlightEntity)
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
    ): Flow<List<HighlightEntity>> {
        return highlightDao.getPageHighlights(bookId, chapterNumber, pageNumber)
    }

    override suspend fun getHighlightsForBook(bookId: String): Flow<List<Highlight>> = flow {
        val localHighlightsForBook = withContext(defaultDispatcher) {
            highlightDao.getHighlightsForBook(bookId)
        }
        emit(localHighlightsForBook.map { it.toHighlight() })

//        try {
//            val dtoHighlightsForBook = withContext(defaultDispatcher) {
//                inputApi.getHighlights(bookId)
//            }
        // Check if the server response is valid and complete
       // if (dtoHighlightsForBook.isNotEmpty() && /* Add any other validation checks */) {
//            val updatedHighlightsForBook = dtoHighlightsForBook.map { dtoHighlight ->
//                val localHighlight = highlightDao.getHighlightById(dtoHighlight.id)
//                if (dtoHighlight.is_deleted == true) {
//                    if (localHighlight != null) {
//                        highlightDao.deleteByIds(dtoHighlight.id)
//                    }
//                } else {
//                    highlightDao.insert(dtoHighlight.toHighlightEntity())
//                }
//                dtoHighlight.toHighlightEntity().toHighlight()
//            }
//            emit(updatedHighlightsForBook)
//        } catch (e: Exception) {
//            //handle api errors, but don't emit anything
//
//        }
    }

}