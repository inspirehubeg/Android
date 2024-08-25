package ih.tools.readingpad

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.ReadingPadRoutes

@Composable
fun ReadingPadNavigation(viewModel: BookContentViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ReadingPadRoutes.CustomThemeScreen.route
    ) {
//        composable(
//            route = ReadingPadRoutes.CustomThemeScreen.route
//        ) {
//            CustomThemeScreen()
//        }
    }
}