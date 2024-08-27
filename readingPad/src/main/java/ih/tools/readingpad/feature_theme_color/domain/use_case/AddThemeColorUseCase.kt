package ih.tools.readingpad.feature_theme_color.domain.use_case

import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository

class AddThemeColorUseCase(private val themeColorRepository: ThemeColorRepository) {
    suspend operator fun invoke(argb: Int, type: ThemeColorType) {
        val themeColor = ThemeColor(argb = argb, type = type)
        themeColorRepository.insert(themeColor)
    }
}