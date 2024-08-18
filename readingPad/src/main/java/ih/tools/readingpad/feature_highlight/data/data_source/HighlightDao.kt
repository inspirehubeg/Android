package ih.tools.readingpad.feature_highlight.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import kotlinx.coroutines.flow.Flow

@Dao
interface HighlightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(highlight: Highlight) : Long

    @Delete
    suspend fun delete(highlight: Highlight)

    @Query("DELETE FROM highlights WHERE id = :highlightId")
    suspend fun deleteByIds(highlightId: Long)

    @Query("SELECT * FROM highlights WHERE bookId = :bookId AND chapterNumber = :chapterNumber AND pageNumber = :pageNumber")
    fun getPageHighlights(
        bookId: String,
        chapterNumber: Int,
        pageNumber: Int
    ): Flow<List<Highlight>>
}