package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

class DeleteThemeColorUseCase(private val themeColorRepository: ThemeColorRepository) {
    suspend operator fun invoke(themeColor: ThemeColor) {
        themeColorRepository.delete(themeColor)
    }
}