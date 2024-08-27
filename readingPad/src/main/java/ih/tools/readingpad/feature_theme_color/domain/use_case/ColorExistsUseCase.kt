package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

class ColorExistsUseCase(private val themeColorRepository: ThemeColorRepository) {
    suspend operator fun invoke(argb: Int): Int {
        return themeColorRepository.colorExists(argb)
    }
}
