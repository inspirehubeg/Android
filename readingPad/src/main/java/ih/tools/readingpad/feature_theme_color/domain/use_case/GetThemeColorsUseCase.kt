package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

/**
 * Use case for retrieving all theme colors of a specific type from the repository.
 *
 * @property themeColorRepository The ThemeColorRepository used to interact with theme color data.
 */
class GetThemeColorsUseCase(private val themeColorRepository: ThemeColorRepository) {
    /**
     * Retrieves all theme colors of the specified type.
     *
     * @param type The type of theme color (background or font) to retrieve.
     * @return A list of theme colors matching the specified type.
     */
    suspend operator fun invoke(type: ThemeColorType): List<ThemeColor> {
        return themeColorRepository.getAll(type)
    }
}