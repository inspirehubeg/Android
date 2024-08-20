package ih.tools.readingpad.feature_book_parsing.presentation.reading_pad_screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_book_parsing.presentation.components.CustomFontDialog
import ih.tools.readingpad.feature_book_parsing.presentation.components.FullScreenImage
import ih.tools.readingpad.feature_book_parsing.presentation.components.PageSelector
import ih.tools.readingpad.feature_book_parsing.presentation.components.ReadingPadBottomBar
import ih.tools.readingpad.feature_book_parsing.presentation.components.ReadingPadTopBar
import ih.tools.readingpad.feature_book_parsing.presentation.components.ThemeSelectorMenu
import ih.tools.readingpad.feature_bookmark.presentation.AddBookmarkDialog
import ih.tools.readingpad.feature_bookmark.presentation.BookmarkListDialog
import ih.tools.readingpad.feature_bookmark.presentation.EditBookmarkDialog
import ih.tools.readingpad.ui.theme.ReadingPadTheme
import kotlinx.coroutines.launch

/** The main ReadingPad screen where the top and bottom bars,
 *  the pages content and all the dialogs ars displayed*/
@Composable
fun ReadingPadScreen(
    viewModel: BookContentViewModel = hiltViewModel(),
    navController: NavController
) {
    val isDarkTheme = viewModel.darkTheme.collectAsState().value

    ReadingPadTheme(isDarkTheme) {
        val showTopBar by viewModel.showTopBar.collectAsState()
        val pinnedTopBar by viewModel.pinnedTopBar.collectAsState()

        val showFontSlider by viewModel.showFontSlider.collectAsState()
        val showThemeSelector by viewModel.showThemeSelector.collectAsState()
        val showBookmarkListDialog by viewModel.showBookmarkListDialog.collectAsState()
        val showPageNumberDialog by viewModel.showPageNumberDialog.collectAsState()

        val showAddBookmarkDialog by viewModel.showAddBookmarkDialog.collectAsState()
        val showEditBookmarkDialog by viewModel.showEditBookmarkDialog.collectAsState()
        val currentBookmarkSpan by viewModel.bookmarkClickEvent.collectAsState()

        val imageClicked by viewModel.imageClicked.collectAsState()
        var showFullScreenImage by remember { mutableStateOf(false) }

        val listState = rememberLazyListState() //monitors the state of the lazyColumn to use in navigation


        // to calculate the height and width of the screen
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp
        val screenWidth = configuration.screenWidthDp
        val dialogHeight = screenHeight * 0.7f
        val dialogWidth = screenWidth * 0.9f


        // this happens whenever there is a bookmark span in focus
        LaunchedEffect(key1 = viewModel, key2 = listState) {
            launch {
                viewModel.bookmarkClickEvent.collect { span ->
                    viewModel._bookmarkClickEvent.value = span
                    Log.d("BookContentScreen", "currentSpan = $currentBookmarkSpan")
                    if (span != null) {
                        //check if it's a new bookmark or an existing one to determine which dialog to open
                        if (viewModel.editBookmark.value) {
                            viewModel._showEditBookmarkDialog.value = true
                            viewModel._editBookmark.value = false
                        } else {
                            viewModel._showAddBookmarkDialog.value = true
                        }
                    }
                }
            }
            launch {
                snapshotFlow { listState.isScrollInProgress }.collect { isScrolling ->
                    if (isScrolling) {
                        viewModel._showTopBar.value = false
                    }
                }
            }
        }

        Scaffold(
            containerColor = ReadingPadTheme.colorScheme.surface,
            topBar = {
                //the showTopBar changes with the single tap on screen to show or hide the bars
                // and emulates full screen effect
                if (pinnedTopBar || showTopBar) {
                    ReadingPadTopBar(navController = navController, viewModel = viewModel)
                }
            },
            bottomBar = {
                if (pinnedTopBar || showTopBar) {
                    ReadingPadBottomBar(viewModel = viewModel, navController)
                }
            }

        ) { values ->
            // passing the values to the lazyColumn or to the Box doesn't allow the topBar to be above the page
            // which is not the intended behavior so don't give this modifier to the lazyColumn
            Box(
                modifier = if (pinnedTopBar){
                    Modifier
                        .fillMaxSize()
                        .padding(values) //this allows the top bar to be pinned without hiding any content beneath it
                } else {
                    Modifier
                        .fillMaxSize()
                        .systemBarsPadding() // this allows the bars to appear above the page to give the full screen behavior
                }
            ) {
/*                this comment for using the recycler view instead of lazy column
//                XMLView(
//                    context = LocalContext.current, viewModel,
//                    modifier = Modifier
//                        .padding(values)
//
                )
*/
                PagesScreen(
                    bookContentViewModel = viewModel,
                    listState = listState
                )
            }

            if (showFontSlider) {
                CustomFontDialog(viewModel)
            }

            if (showThemeSelector) {
                ThemeSelectorMenu(viewModel)
            }
            if (showEditBookmarkDialog) {
                EditBookmarkDialog(viewModel)
            }

            if (showAddBookmarkDialog) {
                AddBookmarkDialog(viewModel)
            }

            if (showBookmarkListDialog) {
                BookmarkListDialog(viewModel, dialogHeight.dp, dialogWidth.dp, listState)
            }

            if (showPageNumberDialog) {
                PageSelector(viewModel, listState)
            }

            if (imageClicked != null) {
                showFullScreenImage = true
            }

            if (showFullScreenImage) {
                FullScreenImage(imageData = imageClicked!!) {
                    //onClose do the following
                    showFullScreenImage = false
                    viewModel.onImageClick(null) // Reset the clicked image state
                }
            }
        }
        BackHandler {
            if (showFontSlider) {
                viewModel._showFontSlider.value = (false)
            } else if (showThemeSelector) {
                viewModel._showThemeSelector.value = false
            } else if (showBookmarkListDialog) {
                viewModel._showBookmarkListDialog.value = false
            } else if (showPageNumberDialog) {
                viewModel._showPageNumberDialog.value = false
            } else if (showAddBookmarkDialog) {
                viewModel._showAddBookmarkDialog.value = false
            } else if (showEditBookmarkDialog) {
                viewModel._showEditBookmarkDialog.value = false
            }else if (showFullScreenImage) {
                showFullScreenImage = false
                viewModel.onImageClick(null)
            }
            else {
                // Let the default back navigation handle this case
                navController.navigateUp()
            }
        }
    }
}





