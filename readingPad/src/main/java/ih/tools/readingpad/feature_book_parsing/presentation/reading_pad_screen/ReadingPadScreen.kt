package ih.tools.readingpad.feature_book_parsing.presentation.reading_pad_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
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
import ih.tools.readingpad.feature_book_parsing.presentation.components.CustomFontDialog
import ih.tools.readingpad.feature_book_parsing.presentation.components.CustomMenu
import ih.tools.readingpad.feature_book_parsing.presentation.components.CustomThemeScreen
import ih.tools.readingpad.feature_book_parsing.presentation.components.FullScreenImage
import ih.tools.readingpad.feature_book_parsing.presentation.components.FullScreenImageTopBar
import ih.tools.readingpad.feature_book_parsing.presentation.components.NavigationDrawer
import ih.tools.readingpad.feature_book_parsing.presentation.components.PageSelector
import ih.tools.readingpad.feature_book_parsing.presentation.components.PagesSlider
import ih.tools.readingpad.feature_book_parsing.presentation.components.ReadingPadBottomBar
import ih.tools.readingpad.feature_book_parsing.presentation.components.ReadingPadTopBar
import ih.tools.readingpad.feature_book_parsing.presentation.components.ThemeSelectorMenu
import ih.tools.readingpad.feature_book_parsing.presentation.components.UserInputScreen
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
    //val isDarkTheme = viewModel.darkTheme.collectAsState().value
    val uiSettings by uiStateViewModel.uiSettings.collectAsState()
    val isDarkTheme = uiSettings.darkTheme

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // val isDrawerOpen by viewModel.isDrawerOpen.collectAsState()
    val isDrawerOpen = uiSettings.isDrawerOpen // Migrated state observation
    val currentDialog by uiStateViewModel.currentDialog.collectAsState()
    val currentScreen by uiStateViewModel.currentScreen.collectAsState()

    LaunchedEffect(key1 = isDrawerOpen) {
        if (isDrawerOpen) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }
    LaunchedEffect (key1 = drawerState.currentValue){
        if (drawerState.currentValue == DrawerValue.Closed) {
            uiStateViewModel.setIsDrawerOpen(false)
        }
    }
    ReadingPadTheme(isDarkTheme) {
        //val showTopBar by viewModel.showTopBar.collectAsState()
        val showTopBar by uiStateViewModel.showTopBar.collectAsState() // Migrated state observation
        // val pinnedTopBar by viewModel.pinnedTopBar.collectAsState()
        val pinnedTopBar = uiSettings.pinnedTopBar // Migrated state observation
        val imageClicked by uiStateViewModel.imageClicked.collectAsState() // Migrated state observation

        //val showFullScreenImage by viewModel.showFullScreenImage.collectAsState()
        val showFullScreenImage = uiStateViewModel.currentScreen.collectAsState()
            .value == UIStateViewModel.ScreenType.FullScreenImage // Migrated state observation

        //val openUserInputScreen by viewModel.showUserInputPage.collectAsState()
        val openUserInputScreen = uiStateViewModel.currentScreen.collectAsState()
            .value == UIStateViewModel.ScreenType.UserInput // Migrated state observation

        //val openCustomTheme by viewModel.showCustomThemePage.collectAsState()
        //val backgroundColor by viewModel.backgroundColor.collectAsState()
        val backgroundColor = uiSettings.backgroundColor // Migrated state observation
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
        val screenHeight = configuration.screenHeightDp
        val dialogWidth = screenWidth * 0.9f


        var showMenu by remember { mutableStateOf(false) }
        val selectionStart by uiStateViewModel.selectionStart.collectAsState()
        val selectionEnd by uiStateViewModel.selectionEnd.collectAsState()
        val menuPosition by uiStateViewModel.menuPosition.collectAsState()
        // Use a LaunchedEffect to listen for selection changes from the ViewModel
        LaunchedEffect(selectionStart, selectionEnd) {
            showMenu = uiStateViewModel.selectionStart.value!= -1 && uiStateViewModel.selectionEnd.value != -1
        }


        // this happens whenever there is a bookmark span in focus
        LaunchedEffect(key1 = viewModel, key2 = listState) {
            launch {
                //viewModel.bookmarkClickEvent.collect
                uiStateViewModel.bookmarkClickEvent.collect { span ->
                    uiStateViewModel.setBookmarkClickEvent(span)
                    if (span != null) {
                        //check if it's a new bookmark or an existing one to determine which dialog to open
                        if (
                        //viewModel.editBookmark.value
                            uiStateViewModel.editBookmark.value
                        ) {
//                            viewModel.setShowEditBookmarkDialog(true)
//                            viewModel.setEditBookmark(false)
                            uiStateViewModel.showDialog(UIStateViewModel.DialogType.EditBookmark) // Migrated event triggering
                            uiStateViewModel.setEditBookmark(false)
                        } else {
                            //viewModel.setShowAddBookmarkDialog(true)
                            uiStateViewModel.showDialog(UIStateViewModel.DialogType.AddBookmark) // Migrated event triggering}
                        }
                    }
                }
            }
            launch {
                //viewModel.noteClickEvent.collect
                uiStateViewModel.noteClickEvent.collect { span ->
                    //viewModel.setNoteClickEvent(span)
                    uiStateViewModel.setNoteClickEvent(span)
                    if (span != null) {
                        //check if it's a new note or an existing one to determine which dialog to open
                        if (
                            uiStateViewModel.editNote.value
                        // viewModel.editNote.value
                        ) {
//                            viewModel.setShowEditNoteDialog(true)
//                            viewModel.setEditNote(false)
                            uiStateViewModel.showDialog(UIStateViewModel.DialogType.EditNote) // Migrated event triggering
                            uiStateViewModel.setEditNote(false)
                        } else {
                            // viewModel.setShowAddNoteDialog(true)
                            uiStateViewModel.showDialog(UIStateViewModel.DialogType.AddNote) // Migrated event triggering
                        }
                    }
                }
            }
            launch {
                snapshotFlow { listState.isScrollInProgress }.collect { isScrolling ->
                    if (isScrolling) {
                        //viewModel.setTopBarVisibility(false)
                        uiStateViewModel.toggleTopBar(false) // Migrated event triggering
                    }
                }
            }
        }

        NavigationDrawer(
            drawerState = drawerState,
            viewModel = viewModel,
            uiStateViewModel = uiStateViewModel,
            content = {
                // Screen content
                Scaffold(containerColor = Color(backgroundColor), topBar = {
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
                        ReadingPadBottomBar(viewModel = viewModel, uiStateViewModel, navController)
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
                            viewModel = viewModel,
                            listState = listState,
                            uiStateViewModel = uiStateViewModel
                        )

                        // user input screen

                        val height =
                            if (openUserInputScreen) LocalConfiguration.current.screenHeightDp.dp else 0.dp
                        val offset =
                            if (openUserInputScreen) 0.dp else LocalConfiguration.current.screenHeightDp.dp


                        UserInputScreen(
                            viewModel = viewModel,
                            uiStateViewModel = uiStateViewModel,
                            height = height,
                            offset = offset
                        )

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

                        UIStateViewModel.DialogType.FontSlider -> CustomFontDialog(
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
//                                viewModel.setDrawerGesturesEnabled(true)
//                                viewModel.setImageRotation(0f)
//                                viewModel.setShowFullScreenImage(false)
//                                viewModel.onImageClick(null) // Reset the clicked image state
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

//                        if (showFontSlider) {
//                            CustomFontDialog(viewModel)
//                        }
//
//                        if (showThemeSelector) {
//                            ThemeSelectorMenu(viewModel)
//                        }
//                        if (showEditBookmarkDialog) {
//                            EditBookmarkDialog(viewModel)
//                        }

//                        if (showAddBookmarkDialog) {
//                            AddBookmarkDialog(viewModel)
//                        }
//                        if (showBrightnessDialog) {
//                            BrightnessDialog(viewModel)
//                        }
//                        if (showAddNoteDialog) {
//                            AddNoteDialog(viewModel = viewModel)
//                        }
//                        if (showEditNoteDialog) {
//                            EditNoteDialog(viewModel = viewModel)
//                        }

//                        if (showBookmarkListDialog) {
//                            BookmarkListDialog(
//                                viewModel,
//                                backgroundHeight = bookmarkDialogHeight.dp,
//                                dialogWidth.dp,
//                                listState
//                            )
//                        }

//                        if (showPageNumberDialog) {
//                            PageSelector(viewModel, listState)
//                        }

                        if (imageClicked != null) {
                            uiStateViewModel.showScreen(UIStateViewModel.ScreenType.FullScreenImage)
//                            viewModel.setShowFullScreenImage(true)
//                            viewModel.setTopBarVisibility(true)
                        }


                    if (showMenu) {
//                        uiStateViewModel.screenWidth = screenWidth
//                        uiStateViewModel.screenHeight = screenHeight
                        CustomMenu(menuPosition, uiStateViewModel, viewModel,menuWidth)
                    }


//                        if (showFullScreenImage) {
//                            viewModel.setDrawerGesturesEnabled(false)
//                            FullScreenImage(viewModel = viewModel,
//                                imageData = imageClicked!!,
//                                onClose = {
//                                    viewModel.setDrawerGesturesEnabled(true)
//                                    viewModel.setImageRotation(0f)
//                                    viewModel.setShowFullScreenImage(false)
//                                    viewModel.onImageClick(null) // Reset the clicked image state
//                                })
//                        }
//                        if (openCustomTheme) {
//                            // ColorPicker(onColorChange = {}, modifier = Modifier)
//                            CustomThemeScreen(
//                                viewModel = viewModel, dialogWidth = dialogWidth.dp
//                            )
//                        }

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
//                        if (showFontSlider) {
//                            viewModel.setShowFontSlider(false)
//                        } else if (showThemeSelector) {
//                            viewModel.setShowThemeSelector(false)
//                        } else if (showBookmarkListDialog) {
//                            viewModel.setShowBookmarkListDialog(false)
//                        } else if (showPageNumberDialog) {
//                            viewModel.setShowPageNumberDialog(false)
//                        } else if (showAddBookmarkDialog) {
//                            viewModel.setShowAddBookmarkDialog(false)
//                        } else if (showEditBookmarkDialog) {
//                            viewModel.setShowEditBookmarkDialog(false)
//                        } else if (showFullScreenImage) {
//                            viewModel.setShowFullScreenImage(false)
//                            viewModel.onImageClick(null)
//                        } else if (openCustomTheme) {
//                            viewModel.setShowCustomThemePage(false)
//                        } else {
//                            // Let the default back navigation handle this case
//                            navController.navigateUp()
//                        }
                }
            }
        )
    }
}




