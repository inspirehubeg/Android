package ih.tools.readingpad.feature_theme_color.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType

/**
 * Data Access Object (DAO) for interacting with the `theme_color_table` in the database.
 */
@Dao
interface ThemeColorDao {
    /**
     * Retrieves all theme colors of a specific type from the database.
     *
     * @param type The type of theme color (background or font).
     * @return A list of theme colors matching the specified type.
     */
    @Query("SELECT * FROM theme_color_table WHERE type = :type")
    suspend fun getAll(type: ThemeColorType): List<ThemeColor>

    /**
     * Inserts a new theme color into the database.
     * If a theme color with the same primary key already exists, it will be replaced.*
     * @param color The theme color object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(color: ThemeColor)

    /**
     * Deletes a theme color from the database.
     *
     * @param color The theme color object to delete.
     */
    @Delete
    suspend fun delete(color: ThemeColor)

    /**
     * Deletes all theme colors of a specific type from the database.
     *
     * @param type The type of theme color (background or font).
     */
    @Query("DELETE FROM theme_color_table WHERE type = :type")
    suspend fun deleteAll(type: ThemeColorType)

    /**
     * Checks if a color with the given ARGB value exists in the database.
     *
     * @param argb The ARGB color value to check.
     * @return The count of colors with the specified ARGB value (1 if it exists, 0 otherwise).
     */
    @Query("SELECT COUNT(*) FROM theme_color_table WHERE argb = :argb")
    suspend fun colorExists(argb: Int): Int
}