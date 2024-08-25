package ih.tools.readingpad.feature_note_color.domain.use_case

data class ThemeColorUseCases(
    val addThemeColorUseCase: AddThemeColorUseCase,
    val getThemeColorsUseCase: GetThemeColorsUseCase,
    val deleteAllThemeColorsUseCase: DeleteAllThemeColorsUseCase,
    val deleteThemeColorUseCase: DeleteThemeColorUseCase,
    val colorExistsUseCase: ColorExistsUseCase
)
