package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

/**
 * Use case for checking if a color with a given ARGB value exists in the repository.
 *
 *@property themeColorRepository The ThemeColorRepository used to interact with theme color data.
 */
class ColorExistsUseCase(private val themeColorRepository: ThemeColorRepository) {
    /**
     * Checks if a color with the given ARGB value exists.
     *
     * @param argb The ARGB color value to check.
     * @return The ID of the color if it exists, 0 otherwise.
     */
    suspend operator fun invoke(argb: Int): Int {
        return themeColorRepository.colorExists(argb)
    }
}
