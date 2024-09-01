package ih.tools.readingpad.feature_theme_color.data.repository

import ih.tools.readingpad.feature_theme_color.data.data_source.ThemeColorDao
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

/**
 * Concrete implementation of the ThemeColorRepository interface.
 * Uses a ThemeColorDao to interact with the database and perform theme color operations.
 *
 * @property dao The ThemeColorDao used to access and manipulate theme color data in the database.
 */
class ThemeColorRepositoryImpl(private val dao: ThemeColorDao) : ThemeColorRepository {

    /**
     * Retrieves all theme colors of a specific type from the database using the dao.
     *
     * @param type The type of theme color (background or font).
     * @return A list of theme colors matching the specified type.
     */
    override suspend fun getAll(type: ThemeColorType): List<ThemeColor> {
        return dao.getAll(type)
    }/**
     * Inserts a new theme color into the database using the dao.
     *
     * @param themeColor The theme color object to insert.
     */
    override suspend fun insert(themeColor: ThemeColor) {
        dao.insert(themeColor)
    }

    /**
     * Deletes a theme color from the database using the dao.
     *
     * @param themeColor The theme color object to delete.
     */
    override suspend fun delete(themeColor: ThemeColor) {
        dao.delete(themeColor)
    }

    /**
     * Deletes all theme colors of a specific type from the database using the dao.
     *
     * @param type The type of theme color (background or font).
     */
    override suspend fun deleteAll(type: ThemeColorType) {
        dao.deleteAll(type)
    }

    /**
     * Checks if a color with the given ARGB value exists in the database using the dao.
     *
     * @param argb The ARGB color value to check.
     * @return The ID of the color if it exists, 0 otherwise.*/
    override suspend fun colorExists(argb: Int): Int {
        return dao.colorExists(argb)
    }
}