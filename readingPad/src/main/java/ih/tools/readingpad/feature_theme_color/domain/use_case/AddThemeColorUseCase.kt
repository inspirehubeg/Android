package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

/**
 * Use case for adding a new theme color to the repository.
 *
 * @property themeColorRepository The ThemeColorRepository used to interact with theme color data.
 */
class AddThemeColorUseCase(private val themeColorRepository: ThemeColorRepository) {
    /**
     * Adds a new theme color to the repository.
     *
     * @param argb The ARGB color value of the theme color.
     * @param type The type of the theme color (background or font).
     */
    suspend operator fun invoke(argb: Int, type: ThemeColorType) {
        val themeColor = ThemeColor(argb = argb, type = type)
        themeColorRepository.insert(themeColor)}
}