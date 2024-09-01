package ih.tools.readingpad.feature_theme_color.domain.repository

import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType

/**
 * Repository interface for managing theme colors.
 * Provides an abstraction layer for accessing and manipulating theme color data.
 */
interface ThemeColorRepository {

    /*** Retrieves all theme colors of a specific type.
     *
     * @param type The type of theme color (background or font).
     * @return A list of theme colors matching the specified type.
     */
    suspend fun getAll(type: ThemeColorType): List<ThemeColor>

    /**
     * Inserts a new theme color.
     *
     * @param themeColor The theme color object to insert.
     */
    suspend fun insert(themeColor: ThemeColor)

    /**
     * Deletes a theme color.
     *
     * @param themeColor The theme color object to delete.
     */
    suspend fun delete(themeColor: ThemeColor)

    /**
     * Deletes all theme colors of a specific type.
     *
     * @param type The type of theme color (background or font).
     */
    suspend fun deleteAll(type: ThemeColorType)

    /**
     * Checks if a color with the given ARGB value exists.
     *
     * @param argb The ARGB color value to check.
     * @return The ID of the color if it exists, 0 otherwise.
     */
    suspend fun colorExists(argb: Int): Int
}