package ih.tools.readingpad.feature_highlight.domain.repository

import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.model.HighlightEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing highlights.
 * Provides an abstraction layer for accessing and manipulating highlight data.
 */
interface HighlightRepository {
    /**
     * Inserts a new highlight.*
     * @param highlightEntity The highlight object to insert.
     * @return The ID of the inserted highlight.
     */
    suspend fun insertHighlight(highlightEntity: HighlightEntity): Long
    /**
     * Deletes a highlight by its ID.
     *
     * @param id The ID of the highlight to delete.
     */
    suspend fun deleteHighlightById(id: Long)
    /**
     * Retrieves all highlights for a specific page within a book.
     *
     * @param bookId The ID of the book.
     * @param chapterNumber The chapter number.
     * @param pageNumber The page number.
     * @return A Flow emitting a list of highlights for the specified page.
     */
    suspend fun getPageHighlights(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<HighlightEntity>>

   suspend fun getHighlightsForBook(bookId: String): Flow<List<Highlight>>
}