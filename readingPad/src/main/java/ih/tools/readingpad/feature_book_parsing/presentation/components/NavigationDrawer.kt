package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.theme.Beige

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    content: @Composable () -> Unit,
    viewModel: BookContentViewModel
) {
    // to calculate the height and width of the screen
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width((screenWidth * .65f).dp)
            ) {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(selected = selectedTabIndex == 0, onClick = {
                        selectedTabIndex = 0
                    }) {
                        Text(text = "Book Index")
                    }
                    Tab(selected = selectedTabIndex == 1, onClick = {
                        selectedTabIndex = 1
                    }) {
                        Text(text = "User Inputs")
                    }
                }
                // Drawer content with the same background color as the top bar
                Box(
                    modifier = Modifier
                        .background(Beige)
                        .fillMaxSize()

                ) {
                    Column {
                        if (selectedTabIndex == 0) {
                            BookIndexDrawerSheet(viewModel)
                        } else UserInputsDrawerSheet(viewModel)
                    }
                }
            }
        },
        drawerState = drawerState,
        content = content
    )
}

@Composable
fun BookIndexDrawerSheet(
    viewModel: BookContentViewModel,
) {

}

@Composable
fun UserInputsDrawerSheet(
    viewModel: BookContentViewModel
) {

    val lazyListState = viewModel.lazyListState
    var selectedItem by remember { mutableStateOf<String?>(null) }
    var expandedBookmarkMenu by remember { mutableStateOf(false) }
    var expandedHighlightMenu by remember { mutableStateOf(false) }
    var expandedNotesMenu by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        CollapsibleMenu(
            modifier =
            Modifier.fillMaxWidth(),
            selected = selectedItem == "Book Bookmarks",
            title = "Book Bookmarks",
            onItemSelected = {
                selectedItem = "Book Bookmarks"
                expandedBookmarkMenu = !expandedBookmarkMenu
                expandedNotesMenu = false
                expandedHighlightMenu = false
            }
        )
        if (expandedBookmarkMenu) {

            MenuContent {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 2.dp, bottom = 2.dp, end = 16.dp)
                        .weight(1f)
                       // .background(Color.Red)
                ) {
                    val items = viewModel.state.value.bookBookmarks
                    items(items) { bookmark ->
                        Text(text = bookmark.bookmarkTitle,
                            modifier = Modifier.clickable {
                                viewModel.scrollToIndexLazy(
                                    targetPageIndex = bookmark.pageNumber - 1,
                                    targetIndex = bookmark.start,
                                    lazyListState = viewModel.lazyListState
                                )
                                viewModel.closeDrawer()
                            })
                    }
                }
            }

        }


        // Page Highlights Submenu
        CollapsibleMenu(
            modifier =
            Modifier
                .fillMaxWidth(),
            selected = selectedItem == "Book Highlights",
            title = "Book Highlights",
            onItemSelected = {
                selectedItem = "Book Highlights"
                expandedBookmarkMenu = false
                expandedHighlightMenu = !expandedHighlightMenu
                expandedNotesMenu = false
            }
        )

        if (expandedHighlightMenu) {
            MenuContent {
                val items = viewModel.state.value.bookHighlights
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 16.dp, top = 2.dp, bottom = 2.dp, end = 16.dp)
                      //  .background(Color.Blue)
                ) {
                    items(items) { highlight ->
                        Text(text = highlight.text,
                            modifier = Modifier.clickable {
                                viewModel.scrollToIndexLazy(
                                    targetPageIndex = highlight.pageNumber - 1,
                                    targetIndex = highlight.start,
                                    lazyListState = lazyListState
                                )
                                viewModel.closeDrawer()
                            })
                    }
                }
            }
        }


        // Page Notes Submenu
        CollapsibleMenu(
            modifier =
            Modifier.fillMaxWidth(),
            selected = selectedItem == "Book Notes",
            title = "Book Notes",
            onItemSelected = {
                selectedItem = "Book Notes"
                expandedBookmarkMenu = false
                expandedHighlightMenu = false
                expandedNotesMenu = !expandedNotesMenu
            }
        )

        if (expandedNotesMenu) {
            MenuContent {
                LazyColumn(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 2.dp, bottom = 2.dp, end = 16.dp)
                        .fillMaxWidth()
                        .weight(1f)

                ) {
                    val items = viewModel.state.value.bookNotes
                    items(items) { note ->
                        Text(text = note.text,
                            modifier = Modifier.clickable {
                                viewModel.scrollToIndexLazy(
                                    targetPageIndex = note.pageNumber - 1,
                                    targetIndex = note.start,
                                    lazyListState = viewModel.lazyListState
                                )
                                viewModel.closeDrawer()
                            })
                    }
                }
            }
        }
    }
}
