package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

class DeleteAllThemeColorsUseCase(private val themeColorRepository: ThemeColorRepository) {
    suspend operator fun invoke(type: ThemeColorType) {
        themeColorRepository.deleteAll(type)
    }
}