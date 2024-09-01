package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

/**
 * Use case for deleting a specific theme color from the repository.
 *
 * @property themeColorRepository The ThemeColorRepository used to interact with theme color data.
 */
class DeleteThemeColorUseCase(private val themeColorRepository: ThemeColorRepository) {
    /**
     * Deletes the specified theme color.
     *
     * @param themeColor The theme color object to delete.
     */
    suspend operator fun invoke(themeColor: ThemeColor) {
        themeColorRepository.delete(themeColor)
    }
}