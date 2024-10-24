package alexschool.bookreader.ui

import alexschool.bookreader.R
import alexschool.bookreader.network.NetworkViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getBookInfo
import ih.tools.readingpad.ui.theme.Beige
import ih.tools.readingpad.ui.theme.Brown

@Composable
fun HomeScreen(
    networkViewModel: NetworkViewModel,
    navController: NavController
) {

    val context = LocalContext.current
    val bookInfo = getBookInfo(context)

//
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        //contentAlignment = Alignment.Center
    ) {
        BookListScreen(networkViewModel = networkViewModel, navController)
        IconButton(
            modifier = Modifier.align(Alignment.BottomStart),
            onClick = {
                navController.navigate(Screens.SettingsScreen.route)
            }) {
            Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
        }
        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Beige,
                contentColor = Brown
            ),
            onClick = {
                navController.navigate(Screens.BookContentScreen.route + "?bookId=${bookInfo.id}")
            }) {
            Text(text = stringResource(R.string.book_content))
        }


    }


}

@Composable
fun BookListScreen(networkViewModel: NetworkViewModel, navController: NavController) {

    Column() {
        CategoriesRow(networkViewModel = networkViewModel)
        BooksRow(networkViewModel = networkViewModel, navController)
    }
//    if (postResponse.isNotEmpty()) {
//        LazyColumn {
//            items(postResponse) { response ->
//                Text(text = response.title)
//            }
//        }
//    }
//    when (postResponse) {
//        is ApiResult.Success -> {
//            val bookInfoList = (category as ApiResult.Success<List<PostResponse>>).data
//            LazyColumn {
//                items(bookInfoList!!) { category ->
//                    Text(text = category.title)
//                }
//            }
//        }
//        is ApiResult.Error -> {
//            when (postResponse.error) {
//                is AppError.NetworkError -> Text("Network error: ${postResponse.error?.message}")
//                is AppError.HttpError -> Text("HTTP error: ${postResponse.error?.message}")
//                is AppError.SerializationError -> Text("Serialization error: ${postResponse.error?.message}")
//                is AppError.ApplicationError -> Text("Application error: ${postResponse.error?.message}")
//                else -> Text("Error loading book info")
//                //Text(text = "Error loading book info")
//            }
//        }
//        is ApiResult.Loading -> {
//            CircularProgressIndicator()
//        }
//    }
}