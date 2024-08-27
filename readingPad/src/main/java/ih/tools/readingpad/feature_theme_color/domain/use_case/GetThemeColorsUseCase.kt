package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

class GetThemeColorsUseCase(private val themeColorRepository: ThemeColorRepository) {
    suspend operator fun invoke(type: ThemeColorType): List<ThemeColor> {
        return themeColorRepository.getAll(type)
    }
}