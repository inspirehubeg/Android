package alexschool.bookreader

import alexschool.bookreader.ui.HomeScreen
import alexschool.bookreader.ui.Screens
import alexschool.bookreader.ui.settings.SettingsScreen
import androidx.compose.runtime.Composable

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ih.tools.readingpad.feature_book_parsing.presentation.reading_pad_screen.ReadingPadScreen

@Composable
fun Navigation(viewModel: AlexSchoolViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(
            route = Screens.HomeScreen.route
        ) {
            viewModel.setCurrentScreen("HomeScreen")
            HomeScreen(navController = navController)
        }
        composable(
            route = Screens.BookContentScreen.route + "?bookId={bookId}",
            arguments = listOf(
                navArgument(
                    name = "bookId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            viewModel.setCurrentScreen("ReadingPadScreen")
            ReadingPadScreen(navController = navController)
        }
//        composable(
//            route = Screen.BookmarksScreen.route
//        ) {
//            BookmarksScreen(navController)
//        }
//        composable(
//            route = Screen.SearchScreen.route
//        ) {
//            SearchScreen(navController = navController)
//        }
        composable(
            route = Screens.SettingsScreen.route
        ) {
            viewModel.setCurrentScreen("SettingsScreen")
            SettingsScreen(navController)
        }
    }
}