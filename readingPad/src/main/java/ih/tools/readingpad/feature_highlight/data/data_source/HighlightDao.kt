package ih.tools.readingpad.feature_highlight.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for interacting with the `highlights` table in the database.
 */
@Dao
interface HighlightDao {
    /**
     * Inserts a new highlight into the database.
     * If a highlight with the same primary key already exists, it will be replaced.
     *
     * @param highlight The highlight object to insert.
     * @return The ID of the inserted highlight.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(highlight: Highlight) : Long

    /**
     * Deletes a highlight from the database.
     *
     * @param highlight The highlight object to delete.
     */
    @Delete
    suspend fun delete(highlight: Highlight)

    /**
     * Deletes highlights from the database by their IDs.
     *
     * @param highlightId The ID of the highlight to delete.
     */
    @Query("DELETE FROM highlights WHERE id = :highlightId")
    suspend fun deleteByIds(highlightId: Long)

    /**
     * Retrieves all highlights for a specific page within a book.
     *
     * @param bookId The ID of the book.
     * @param chapterNumber The chapter number.
     * @param pageNumber The page number.
     * @return A Flow emitting a list of highlights for the specified page.
     */
    @Query("SELECT * FROM highlights WHERE bookId = :bookId AND chapterNumber = :chapterNumber AND pageNumber = :pageNumber")
    fun getPageHighlights(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Highlight>>


    /**
     * Retrieves all highlights for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A Flow emitting a list of highlights for the book.
     */
    @Query("SELECT * FROM highlights WHERE bookId = :bookId")
     fun getHighlightsForBook(bookId: String): Flow<List<Highlight>>
}