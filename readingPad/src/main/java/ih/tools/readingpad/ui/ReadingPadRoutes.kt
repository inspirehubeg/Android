package ih.tools.readingpad.ui

sealed class ReadingPadRoutes(val route: String){
    object CustomThemeScreen: ReadingPadRoutes("custom_theme")
}