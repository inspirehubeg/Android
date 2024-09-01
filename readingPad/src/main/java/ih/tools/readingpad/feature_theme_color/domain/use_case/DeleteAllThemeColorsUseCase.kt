package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

/**
 * Use case for deleting all theme colors of a specific type from the repository.
 *
 * @property themeColorRepository The ThemeColorRepository used to interact with theme color data.
 */
class DeleteAllThemeColorsUseCase(private val themeColorRepository: ThemeColorRepository) {
    /**
     * Deletes all theme colors of the specified type.
     *
     * @param type The type of theme color (background or font) to delete.
     */
    suspend operator fun invoke(type: ThemeColorType) {
        themeColorRepository.deleteAll(type)
    }
}