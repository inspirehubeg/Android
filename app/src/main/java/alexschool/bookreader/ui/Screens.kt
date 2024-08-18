package alexschool.bookreader.ui

sealed class Screens(val route: String){
    object HomeScreen: Screens("home_screen")
    object BookContentScreen: Screens("book_content_screen")
    object BooksScreen: Screens("books_screen")
    object FavoriteBooksScreen: Screens("fav_books_screen")
    object BookmarksScreen: Screens("bookmarks_screen")
    object RecentReadsScreen: Screens("recent_reads_screen")
    object TrendingBooksScreen: Screens("trending_books_screen")
    object SearchScreen: Screens("search_screen")
    object SettingsScreen: Screens("settings_screen")
    object ProfileScreen: Screens("profile_screen")

}