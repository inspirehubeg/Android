package ih.tools.readingpad.feature_book_parsing.presentation.reading_pad_screen

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_book_parsing.presentation.components.BrightnessDialog
import ih.tools.readingpad.feature_book_parsing.presentation.components.CustomThemeScreen
import ih.tools.readingpad.feature_book_parsing.presentation.components.FontSizeSlider
import ih.tools.readingpad.feature_book_parsing.presentation.components.FullScreenImage
import ih.tools.readingpad.feature_book_parsing.presentation.components.FullScreenImageTopBar
import ih.tools.readingpad.feature_book_parsing.presentation.components.InlineSelectionMenu
import ih.tools.readingpad.feature_book_parsing.presentation.components.NavigationDrawer
import ih.tools.readingpad.feature_book_parsing.presentation.components.PageSelector
import ih.tools.readingpad.feature_book_parsing.presentation.components.PagesSlider
import ih.tools.readingpad.feature_book_parsing.presentation.components.ReadingPadBottomBar
import ih.tools.readingpad.feature_book_parsing.presentation.components.ReadingPadTopBar
import ih.tools.readingpad.feature_book_parsing.presentation.components.ThemeSelectorMenu
import ih.tools.readingpad.feature_bookmark.presentation.AddBookmarkDialog
import ih.tools.readingpad.feature_bookmark.presentation.BookmarkListDialog
import ih.tools.readingpad.feature_bookmark.presentation.EditBookmarkDialog
import ih.tools.readingpad.feature_note.presentation.AddNoteDialog
import ih.tools.readingpad.feature_note.presentation.EditNoteDialog
import ih.tools.readingpad.ui.UIStateViewModel
import ih.tools.readingpad.ui.theme.ReadingPadTheme
import kotlinx.coroutines.launch

/** The main ReadingPad screen where the top and bottom bars,
 *  the pages content and all the dialogs ars displayed*/
@Composable
fun ReadingPadScreen(
    viewModel: BookContentViewModel = hiltViewModel(),
    uiStateViewModel: UIStateViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiSettings by uiStateViewModel.uiSettings.collectAsState()
    val isDarkTheme = uiSettings.darkTheme

    val currentDialog by uiStateViewModel.currentDialog.collectAsState()
    val currentScreen by uiStateViewModel.currentScreen.collectAsState()


//    LaunchedEffect (key1 = drawerState.currentValue){
//        if (drawerState.currentValue == DrawerValue.Closed) {
//            uiStateViewModel.setIsDrawerOpen(false)
//        }
//    }
    ReadingPadTheme(isDarkTheme) {
        val showTopBar by uiStateViewModel.showTopBar.collectAsState()
        val pinnedTopBar = uiSettings.pinnedTopBar
        val imageClicked by uiStateViewModel.imageClicked.collectAsState()

        val showFullScreenImage = uiStateViewModel.currentScreen.collectAsState()
            .value == UIStateViewModel.ScreenType.FullScreenImage
        val backgroundColor = uiSettings.backgroundColor
        val listState = uiStateViewModel.lazyListState
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current
        val menuWidth = 200.dp
        // Get screen width in pixels
        val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
        uiStateViewModel.setScreenWidth(screenWidthPx)

        // Get menu width in pixels (replace with your actual menu width)
        val menuWidthPx = with(density) { 200.dp.toPx() }
        uiStateViewModel.setMenuWidth(menuWidthPx)

        // to calculate the height and width of the screen
        val screenWidth = configuration.screenWidthDp
        val dialogWidth = screenWidth * 0.9f


        var showMenu by remember { mutableStateOf(false) }
        val selectionStart by uiStateViewModel.selectionStart.collectAsState()
        val selectionEnd by uiStateViewModel.selectionEnd.collectAsState()
        val menuPosition by uiStateViewModel.menuPosition.collectAsState()
        // Use a LaunchedEffect to listen for selection changes from the ViewModel
        LaunchedEffect(selectionStart, selectionEnd) {
            showMenu =
                uiStateViewModel.selectionStart.value != -1 && uiStateViewModel.selectionEnd.value != -1
        }


        // this happens whenever there is a bookmark span in focus
        LaunchedEffect(key1 = viewModel, key2 = listState) {
            launch {
                uiStateViewModel.bookmarkClickEvent.collect { span ->
                    uiStateViewModel.setBookmarkClickEvent(span)
                    if (span != null) {
                        //check if it's a new bookmark or an existing one to determine which dialog to open
                        if (
                            uiStateViewModel.editBookmark.value
                        ) {
                            uiStateViewModel.showDialog(UIStateViewModel.DialogType.EditBookmark)
                            uiStateViewModel.setEditBookmark(false)
                        } else {
                            uiStateViewModel.showDialog(UIStateViewModel.DialogType.AddBookmark)
                        }
                    }
                }
            }
            launch {
                uiStateViewModel.noteClickEvent.collect { span ->
                    uiStateViewModel.setNoteClickEvent(span)
                    if (span != null) {
                        //check if it's a new note or an existing one to determine which dialog to open
                        if (
                            uiStateViewModel.editNote.value
                        ) {
                            uiStateViewModel.showDialog(UIStateViewModel.DialogType.EditNote)
                            uiStateViewModel.setEditNote(false)
                        } else {
                            uiStateViewModel.showDialog(UIStateViewModel.DialogType.AddNote)
                        }
                    }
                }
            }
            launch {
                snapshotFlow { listState.isScrollInProgress }.collect { isScrolling ->
                    if (isScrolling) {
                        uiStateViewModel.toggleTopBar(false)
                    }
                }
            }
        }
        NavigationDrawer(
            viewModel = viewModel,
            uiStateViewModel = uiStateViewModel,
            content = {
                // Screen content
                Scaffold(
                    containerColor = Color(backgroundColor),
                    topBar = {
                        //the showTopBar changes with the single tap on screen to show or hide the bars
                        // and emulates full screen effect
                        if (pinnedTopBar || showTopBar) {
                            ReadingPadTopBar(
                                navController = navController,
                                viewModel = viewModel,
                                uiStateViewModel = uiStateViewModel
                            )
                        }
                        if (showFullScreenImage) {
                            FullScreenImageTopBar(viewModel = viewModel, uiStateViewModel)
                        }
                    }, bottomBar = {
                        if (pinnedTopBar || showTopBar) {
                            ReadingPadBottomBar(
                                viewModel = viewModel,
                                uiStateViewModel,
                                navController
                            )
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
                  // Column (modifier = Modifier.fillMaxSize()){
                        PagesScreen(
                            viewModel = viewModel,
                            listState = listState,
                            uiStateViewModel = uiStateViewModel,
                            modifier = Modifier
                        )
//                            Spacer(modifier = Modifier.height(4.dp))
//                            PagesScreen(
//                                viewModel = viewModel,
//                                uiStateViewModel = uiStateViewModel,
//                                listState = listState,
//                                modifier = Modifier.weight(1f)
//                            )

                     //   }
                    }
                    when (currentDialog) {
                        UIStateViewModel.DialogType.Brightness -> BrightnessDialog(
                            uiStateViewModel
                        )

                        UIStateViewModel.DialogType.BookmarkList -> BookmarkListDialog(
                            viewModel = viewModel,
                            dialogWidth = dialogWidth.dp,
                            listState = listState,
                            uiStateViewModel = uiStateViewModel
                        )

                        UIStateViewModel.DialogType.PageNumber -> PageSelector(
                            viewModel,
                            listState,
                            uiStateViewModel
                        )

                        UIStateViewModel.DialogType.AddBookmark -> AddBookmarkDialog(
                            viewModel,
                            uiStateViewModel
                        )

                        UIStateViewModel.DialogType.EditBookmark -> EditBookmarkDialog(
                            viewModel,
                            uiStateViewModel
                        )

                        UIStateViewModel.DialogType.AddNote -> AddNoteDialog(
                            viewModel = viewModel,
                            uiStateViewModel
                        )

                        UIStateViewModel.DialogType.EditNote -> EditNoteDialog(
                            viewModel = viewModel,
                            uiStateViewModel
                        )

                        UIStateViewModel.DialogType.ThemeSelector -> ThemeSelectorMenu(
                            viewModel,
                            uiStateViewModel
                        )

                        UIStateViewModel.DialogType.FontSlider -> FontSizeSlider(
                            viewModel,
                            uiStateViewModel
                        )

                        UIStateViewModel.DialogType.PagesSlider -> PagesSlider(
                            viewModel,
                            uiStateViewModel
                        )

                        null -> {}
                    }


                    when (currentScreen) {
                        UIStateViewModel.ScreenType.FullScreenImage -> FullScreenImage(
                            uiStateViewModel = uiStateViewModel,
                            viewModel = viewModel,
                            imageData = imageClicked!!,
                            onClose = {

                                uiStateViewModel.showScreen(null)
                                uiStateViewModel.onImageClick(null)
                                uiStateViewModel.setAreDrawerGesturesEnabled(true)
                                uiStateViewModel.setImageRotation(0f)
                            })

                        UIStateViewModel.ScreenType.UserInput -> TODO()
                        UIStateViewModel.ScreenType.CustomTheme -> CustomThemeScreen(
                            viewModel = viewModel,
                            dialogWidth = dialogWidth.dp,
                            uiStateViewModel = uiStateViewModel
                        )

                        null -> {}
                    }


                    if (imageClicked != null) {
                        uiStateViewModel.showScreen(UIStateViewModel.ScreenType.FullScreenImage)
                    }


                    if (showMenu) {
                        InlineSelectionMenu(menuPosition, uiStateViewModel, viewModel, menuWidth)
                    }


                }

                BackHandler {
                    if (uiStateViewModel.currentDialog.value != null) {
                        uiStateViewModel.showDialog(null) // Close any open dialog
                    } else if (uiStateViewModel.currentScreen.value != null) {
                        uiStateViewModel.showScreen(null) // Close any open screen
                    } else {
                        // Let the default back navigation handle this case
                        navController.navigateUp()
                    }
                }

            }
        )
    }
}


//
