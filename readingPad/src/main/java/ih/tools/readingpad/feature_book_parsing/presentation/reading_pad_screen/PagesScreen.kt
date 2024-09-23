package ih.tools.readingpad.feature_book_parsing.presentation.reading_pad_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getMetadata
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.convertPagesToSpannedPagesLazy
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel

/** This lazy column represents the inside of the ReadingPadScreen with pages as its items*/
@Composable
fun PagesScreen(
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel,
    listState: LazyListState,
) {
    val context = LocalContext.current
    val book = viewModel.book

    val firstVisibleItemIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }
    viewModel.setPageNumber(firstVisibleItemIndex.value + 1)

    //when first visible item index == 0 then load the previous chapter and add it to the book

    //when first visible item index == itemPages.size -2 then load the next chapter and add it to the book

    val isLoading by viewModel.isLoading.collectAsState()
    //to be changed when a bigger book is available for use
    val currentChapterIndex = 0
    val currentChapter = book.chapters[currentChapterIndex]
    val chapterSpannedPages = remember { mutableStateListOf<SpannedPage>() }

//    LaunchedEffect(key1 = currentChapterIndex) {
//        chapterSpannedPages.clear()
//        chapterSpannedPages.addAll(
//            bookContentViewModel.getSpannedPagesForChapter(currentChapterIndex, currentChapter.pages)
//        )
//    }
    val uiSettings = uiStateViewModel.uiSettings.collectAsState()
    // val pages = mutableListOf<Page>()
    val itemPages = remember {
        mutableStateOf(listOf<SpannedPage>())
    }
    // val itemPages = remember { mutableStateListOf<SpannedPage>() }
    val metadata = getMetadata(context)
    val scrollable by viewModel.oneFingerScroll.collectAsState()
    // to calculate the height and width of the screen
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    val showHighlights = uiSettings.value.showHighlightsBookmarks
//    if (itemPages.value.isNotEmpty() && firstVisibleItemIndex.value >= itemPages.value.size - 2) {
//        Log.d(
//            "PagesScreen",
//            "fetch next chapter called firstVisibleItemIndex = ${firstVisibleItemIndex.value}, itemPages.size = ${itemPages.value.size}"
//        )
//        bookContentViewModel.fetchNextChapter()
//    }


//    if (firstVisibleItemIndex.value == 0){
//        bookContentViewModel.loadPreviousChapter()
//    }
    LaunchedEffect(key1 = viewModel.chapterCount.value) { // Launch a coroutine when 'chapter' changes
        val pages = book.chapters[book.chapters.size - 1].pages
        Log.d("PagesScreen", "chapters size = ${book.chapters.size}")
        val spannedPages = convertPagesToSpannedPagesLazy(
            pages,
            metadata,
            context,
            book,
            listState,
            viewModel,
            uiStateViewModel
        )
        //itemPages.clear()
        if (spannedPages.isNotEmpty()) {
            itemPages.value += spannedPages
            Log.d("PagesScreen", "item pages size = ${itemPages.value.size}")
        } else Log.d("PagesScreen", "spanned pages is empty")
    }

//    if (isLoading) {
//      Display loading indicator
//    Consider using a more informative loading indicator, such as a progress bar that shows the percentage of completion.
//    Provide a way for the user to cancel the loading process if it takes too long.
//    If the loading process involves network requests, handle potential errors gracefully
//    and display appropriate messages to the user.
//        CircularProgressIndicator()
//    } else {
    LazyColumn(
        state = listState,
        userScrollEnabled = scrollable
    ) {
        itemsIndexed(itemPages.value,
            key = { index, page ->
            "${page.pageNumber}-${showHighlights}-$index"
         })
        { index, page ->
                val chapterIndex = viewModel.getCurrentChapterIndex(page.pageNumber)
                XMLViewLazyItem(
                    page = page,
                    viewModel = viewModel,
                    modifier = Modifier,
                    chapterIndex = chapterIndex,
                    uiStateViewModel = uiStateViewModel,
                    itemIndex = index
                )
                Spacer( //indicates the pages divider
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.outline)
                        .height(3.dp)
                )
            }
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









