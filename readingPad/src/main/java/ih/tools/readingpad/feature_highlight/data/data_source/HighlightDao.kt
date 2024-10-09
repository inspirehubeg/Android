package ih.tools.readingpad.feature_highlight.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
     * @param highlightEntity The highlight object to insert.
     * @return The ID of the inserted highlight.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(highlightEntity: HighlightEntity) : Long

    /**
     * Deletes a highlight from the database.
     *
     * @param highlightEntity The highlight object to delete.
     */
    @Delete
    suspend fun delete(highlightEntity: HighlightEntity)

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
    @Query("SELECT * FROM highlights WHERE bookId = :bookId AND chapterNumber = :chapterNumber " +
            "AND pageNumber = :pageNumber AND isDeleted = 0")
    fun getPageHighlights(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<HighlightEntity>>


    /**
     * Retrieves all highlights for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A Flow emitting a list of highlights for the book.
     */
    @Query("SELECT * FROM highlights WHERE bookId = :bookId AND isDeleted = 0")
     fun getHighlightsForBook(bookId: String): List<HighlightEntity>


     @Query("SELECT * FROM highlights WHERE id = :id")
     suspend fun getHighlightById(id: Long): HighlightEntity?



    @Query("SELECT * FROM highlights WHERE serverId = :serverId")
    suspend fun getHighlightByServerId(serverId: String): HighlightEntity?

    @Query("UPDATE highlights SET serverId = :serverId WHERE id = :localId")
    suspend fun updateServerId(localId: Long, serverId: String)

    @Query("DELETE FROM highlights WHERE serverId = :serverId")
    suspend fun deleteHighlightByServerId(serverId: String)

    @Query("SELECT * FROM highlights WHERE serverId IS NULL")
    suspend fun getHighlightsWithoutServerId(): List<HighlightEntity>


    @Query("UPDATE highlights SET isDeleted = 1 WHERE id = :highlightId")
    suspend fun markHighlightAsDeleted(highlightId: Long)

    @Query("SELECT * FROM highlights WHERE isDeleted = 1")
    suspend fun getLocallyDeletedHighlights(): List<HighlightEntity>

}