package ih.tools.readingpad.feature_theme_color.domain.use_case

/**
 * Data class that encapsulates various use cases related to theme color management.
 * This class helps organize and provide access to different theme color operations.
 ** @property addThemeColorUseCase Use case for adding a new theme color.
 * @property getThemeColorsUseCase Use case for retrieving theme colors of a specific type.
 * @property deleteAllThemeColorsUseCase Use case for deleting all theme colors of a specific type.
 * @property deleteThemeColorUseCase Use case for deleting a specific theme color.
 * @property colorExistsUseCase Use case for checking if a color exists.
 */
data class ThemeColorUseCases(
    val addThemeColorUseCase: AddThemeColorUseCase,
    val getThemeColorsUseCase: GetThemeColorsUseCase,
    val deleteAllThemeColorsUseCase: DeleteAllThemeColorsUseCase,
    val deleteThemeColorUseCase: DeleteThemeColorUseCase,
    val colorExistsUseCase: ColorExistsUseCase
)
