package ih.tools.readingpad.feature_book_parsing.presentation.reading_pad_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getMetadata
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.convertPagesToSpannedPagesLazy
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel

/** This lazy column represents the inside of the ReadingPadScreen with pages as its items*/
@Composable
fun PagesScreen(
    bookContentViewModel: BookContentViewModel,
    listState: LazyListState,
) {
    val context = LocalContext.current
    val book = bookContentViewModel.book
    val verticalScroll by bookContentViewModel.verticalScroll.collectAsState()

    val firstVisibleItemIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }
    bookContentViewModel._pageNumber.value = firstVisibleItemIndex.value + 1

    // val pages = mutableListOf<Page>()
    val itemPages = remember { mutableStateListOf<SpannedPage>() }
    val metadata = getMetadata(context)

    // to calculate the height and width of the screen
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    LaunchedEffect(key1 = book) { // Launch a coroutine when 'book' changes
        val pages = book.chapters[book.chapters.size - 1].pages
        val spannedPages = convertPagesToSpannedPagesLazy(
            pages,
            metadata,
            context,
            book,
            listState,
            bookContentViewModel
        )
        itemPages.clear()
        if (spannedPages.isNotEmpty()) {
            itemPages.addAll(spannedPages)
            Log.d("PagesScreen", "spanned pages is not empty ${itemPages[0].pageNumber}")
        } else Log.d("PagesScreen", "spanned pages is empty")


    }
   // if (verticalScroll) {
        LazyColumn(
            state = listState
        ) {
            itemsIndexed(itemPages) { index, page ->
                // Only display the current page
                Log.d("PagesScreen", "page index = $index")
                XMLViewLazyItem(
                    page = page,
                    viewModel = bookContentViewModel,
                    modifier = Modifier
                )
                Spacer( //indicates the pages divider
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.outline)
                        .height(3.dp)
                )
            }
        }
//    } else {
//        LazyRow(
//            state = listState,
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            itemsIndexed(itemPages) { index, page ->
//                Box(
//                    modifier = Modifier
//                        .width(screenWidth.dp)
//                        .height(screenHeight.dp)
//                ) {
//                    val verticalScrollState = rememberScrollState()
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .verticalScroll(verticalScrollState)
//                    ) {
//                        XMLViewLazyItem(
//                            page = page,
//                            viewModel = bookContentViewModel,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                        )
//                        Spacer(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .background(MaterialTheme.colorScheme.outline)
//                                .height(3.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
}









