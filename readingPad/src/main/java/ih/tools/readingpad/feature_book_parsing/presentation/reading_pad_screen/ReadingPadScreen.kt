package ih.tools.readingpad.feature_book_parsing.presentation.reading_pad_screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_book_parsing.presentation.components.CustomFontDialog
import ih.tools.readingpad.feature_book_parsing.presentation.components.CustomThemeScreen
import ih.tools.readingpad.feature_book_parsing.presentation.components.FullScreenImage
import ih.tools.readingpad.feature_book_parsing.presentation.components.FullScreenImageTopBar
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
    viewModel: BookContentViewModel = hiltViewModel(), navController: NavController
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
        val showFullScreenImage by viewModel.showFullScreenImage.collectAsState()

        val openCustomTheme by viewModel.showCustomThemePage.collectAsState()
        val backgroundColor by viewModel.backgroundColor.collectAsState()
        val fontColor by viewModel.fontColor.collectAsState()

        val listState =
            viewModel.lazyListState //monitors the state of the lazyColumn to use in navigation


        // to calculate the height and width of the screen
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp
        val screenWidth = configuration.screenWidthDp
        val bookmarkDialogHeight = screenHeight * 0.6f
        val dialogWidth = screenWidth * 0.9f


        // this happens whenever there is a bookmark span in focus
        LaunchedEffect(key1 = viewModel, key2 = listState) {
            launch {
                viewModel.bookmarkClickEvent.collect { span ->
                    viewModel.setBookmarkClickEvent(span)
                    Log.d("BookContentScreen", "currentSpan = $currentBookmarkSpan")
                    if (span != null) {
                        //check if it's a new bookmark or an existing one to determine which dialog to open
                        if (viewModel.editBookmark.value) {
                            viewModel.setShowEditBookmarkDialog(true)
                            viewModel.setEditBookmark(false)
                        } else {
                            viewModel.setShowAddBookmarkDialog(true)
                        }
                    }
                }
            }
            launch {
                snapshotFlow { listState.isScrollInProgress }.collect { isScrolling ->
                    if (isScrolling) {
                        viewModel.setTopBarVisibility(false)
                    }
                }
            }
        }

        Scaffold(containerColor = Color(backgroundColor), topBar = {
            //the showTopBar changes with the single tap on screen to show or hide the bars
            // and emulates full screen effect
            if (pinnedTopBar || showTopBar) {
                ReadingPadTopBar(navController = navController, viewModel = viewModel)
            }
            if (showFullScreenImage) {
                FullScreenImageTopBar(viewModel = viewModel)
            }
        }, bottomBar = {
            if (pinnedTopBar || showTopBar) {
                ReadingPadBottomBar(viewModel = viewModel, navController)
            }
        }

        ) { values ->
            // passing the values to the lazyColumn or to the Box doesn't allow the topBar to be above the page
            // which is not the intended behavior so don't give this modifier to the lazyColumn
            Box(
                modifier = if (pinnedTopBar) {
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
                    bookContentViewModel = viewModel, listState = listState
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
                BookmarkListDialog(
                    viewModel, backgroundHeight = bookmarkDialogHeight.dp, dialogWidth.dp, listState
                )
            }

            if (showPageNumberDialog) {
                PageSelector(viewModel, listState)
            }

            if (imageClicked != null) {
                viewModel.setShowFullScreenImage(true)
                viewModel.setTopBarVisibility(true)
            }

            if (showFullScreenImage) {
                FullScreenImage(viewModel = viewModel,
                    imageData = imageClicked!!,
                    onClose = {
                        viewModel.setImageRotation(0f)
                        viewModel.setShowFullScreenImage(false)
                        viewModel.onImageClick(null) // Reset the clicked image state
                    })
            }
            if (openCustomTheme) {
                // ColorPicker(onColorChange = {}, modifier = Modifier)
                CustomThemeScreen(
                    viewModel = viewModel, dialogWidth = dialogWidth.dp
                )
            }
        }
        BackHandler {
            if (showFontSlider) {
                viewModel.setShowFontSlider(false)
            } else if (showThemeSelector) {
                viewModel.setShowThemeSelector(false)
            } else if (showBookmarkListDialog) {
                viewModel.setShowBookmarkListDialog(false)
            } else if (showPageNumberDialog) {
                viewModel.setShowPageNumberDialog(false)
            } else if (showAddBookmarkDialog) {
                viewModel.setShowAddBookmarkDialog(false)
            } else if (showEditBookmarkDialog) {
                viewModel.setShowEditBookmarkDialog(false)
            } else if (showFullScreenImage) {
                viewModel.setShowFullScreenImage(false)
                viewModel.onImageClick(null)
            } else if (openCustomTheme) {
                viewModel.setShowCustomThemePage(false)
            } else {
                // Let the default back navigation handle this case
                navController.navigateUp()
            }
        }
    }
}





