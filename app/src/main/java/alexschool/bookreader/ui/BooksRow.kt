package alexschool.bookreader.ui

import alexschool.bookreader.data.domain.Book
import alexschool.bookreader.data.mappers.toBook
import alexschool.bookreader.network.NetworkViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

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
                        onClick = { navController.navigate("${Screens.BookContentScreen.route}?bookId=${book.id}") })
                }
            }
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(4.dp)
            .clickable { onClick() },
    ) {
        Column {
            Icon(imageVector = Icons.Default.Home, contentDescription = "category image")
            Text(text = book.name)
        }
    }
}