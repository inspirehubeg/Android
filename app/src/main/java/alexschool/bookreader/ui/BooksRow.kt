package alexschool.bookreader.ui

import alexSchool.network.mappers.toBook
import alexschool.bookreader.network.NetworkViewModel
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun BooksRow(networkViewModel: NetworkViewModel, navController: NavController) {
    val books by networkViewModel.books.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        if (books.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        } else {

            LazyRow() {
                items(books) { book ->
                    BookItem(
                        book = book.toBook(),
                        onClick = {
//                            navController.currentBackStackEntry?.savedStateHandle?.set(
//                                key = "bookInfo",
//                                value = book
//                            )
//                            navController.navigate(Screens.BookContentScreen.route)
                            navController.navigate("${Screens.BookContentScreen.route}?bookId=${book.id}")
                        })
                }
            }
        }
    }
}


@Composable
fun BookItem(
    book: alexSchool.network.domain.Book,
    onClick: () -> Unit = {}
) {
    val cover = book.cover
    Log.d("BookItem", "BookItem: cover= ${cover?.size}")


    val bitmap = remember(cover) {
        BitmapFactory.decodeByteArray(cover, 0, cover?.size ?: 0)
    }
    Log.d("BookItem", "BookItem: bitmap= ${bitmap}")



    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .clickable { onClick() },
    ) {
        Column (Modifier.fillMaxSize()){
            Image(
                painter = //error(R.drawable.error_image)
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = bitmap)
                        .apply(block = fun ImageRequest.Builder.() {
                            error(android.R.drawable.stat_notify_error)
                            placeholder(android.R.drawable.stat_sys_download)

                        }).build()
                ),
                contentDescription = book.name,
                modifier = Modifier.weight(3f)
            )
            //Icon(imageVector = Icons.Default.Home, contentDescription = "category image")
            Text(text = book.name , modifier = Modifier.weight(1f))
        }
    }
}

