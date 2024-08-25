package ih.tools.readingpad.feature_note_color.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ih.tools.readingpad.feature_note_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_note_color.domain.model.ThemeColorType

@Dao
interface ThemeColorDao {
@Query("SELECT * FROM theme_color_table WHERE type = :type")
    suspend fun getAll(type: ThemeColorType): List<ThemeColor>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(color: ThemeColor)

    @Delete
    suspend fun delete(color: ThemeColor)

    @Query("DELETE FROM theme_color_table WHERE type = :type")
    suspend fun deleteAll(type: ThemeColorType)

    @Query("SELECT COUNT(*) FROM theme_color_table WHERE argb = :argb")
    suspend fun colorExists(argb: Int): Int

}