package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel

@Composable
fun ReadingPadBottomBar(
    viewModel: BookContentViewModel,
    navController: NavController
) {
    val showFontSlider by viewModel.showFontSlider.collectAsState()
    val showThemeSelector by viewModel.showThemeSelector.collectAsState()
    val showBookmarkListDialog by viewModel.showBookmarkListDialog.collectAsState()
    val showPageNumberDialog by viewModel.showPageNumberDialog.collectAsState()
    val pageNumber by viewModel.pageNumber
    val oneFingerScroll by viewModel.oneFingerScroll.collectAsState()
    // the list of bottom bar items, they are displayed in the same order
    val itemList: List<NavigationItem> = listOf(
        // this is just a place holder to be changed later with a working icon
        NavigationItem(
            title = stringResource(R.string.settings),
            selectedIcon = {
                Icon(Icons.Default.Settings, "")
            },
            showDialog = false,
            onClick = {
               // viewModel.setOneFingerScroll(!oneFingerScroll)
                //viewModel.setVerticalScroll(!verticalScroll)
               // viewModel.setShowCustomThemePage(true)
            }
        ),

        NavigationItem(
            title = stringResource(R.string.bookmarks),
            selectedIcon = {
                Icon(Icons.Default.Bookmarks, contentDescription = stringResource(R.string.bookmarks))
            },
            showDialog = showBookmarkListDialog,
            onClick = {
                viewModel.setShowBookmarkListDialog(!showBookmarkListDialog)
                viewModel.setShowThemeSelector(false)
                viewModel.setShowFontSlider(false)
                viewModel.setShowPageNumberDialog(false)
                viewModel.setShowCustomThemePage(false)
            }
        ),

        NavigationItem(
            title = stringResource(R.string.page_number),
            selectedIcon = {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer, // Customize background color
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        text = pageNumber.toString(),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        fontSize = 20.sp
                    )
                }
            },
            showDialog = false,
            onClick = {
                viewModel.setShowPageNumberDialog(!showPageNumberDialog)
                viewModel.setShowBookmarkListDialog(false)
                viewModel.setShowThemeSelector(false)
                viewModel.setShowFontSlider(false)
                viewModel.setShowCustomThemePage(false)

            }
        ),
        NavigationItem(
            title = stringResource(R.string.font_size),
            selectedIcon = {
                Icon(Icons.Default.FormatSize, contentDescription = stringResource(R.string.adjust_font_size))
            },
            showDialog = showFontSlider,
            onClick = {
                viewModel.setShowFontSlider(!showFontSlider)
                viewModel.setShowThemeSelector(false)
                viewModel.setShowBookmarkListDialog(false)
                viewModel.setShowPageNumberDialog(false)
                viewModel.setShowCustomThemePage(false)

            }
        ),
        NavigationItem(
            title = stringResource(R.string.themes),
            selectedIcon = {
                Icon(Icons.Default.ColorLens, contentDescription = stringResource(R.string.choose_a_theme))
            },
            showDialog = showThemeSelector,
            onClick = {
                viewModel.setShowThemeSelector(!showThemeSelector)
                viewModel.setShowFontSlider(false)
                viewModel.setShowBookmarkListDialog(false)
                viewModel.setShowPageNumberDialog(false)
                viewModel.setShowCustomThemePage(false)

            }
        )

    )


// the start of the bottom bar, using surface to give it a custom height
    Surface(modifier = Modifier.height(50.dp)) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            itemList.forEach { navigationItem ->
                NavigationBarItem(
                    modifier = Modifier.padding(4.dp),
                    selected = navigationItem.showDialog,
                    onClick = { navigationItem.onClick() },
                    icon = navigationItem.selectedIcon,
                )
            }
        }
    }
//    Surface(modifier = Modifier.height(48.dp)) { // Adjust height as needed
//        BottomAppBar(
//            containerColor = MaterialTheme.colorScheme.secondaryContainer,
//            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
//        ) {
//            IconButton(
//                modifier = Modifier.weight(1.5f),
//                onClick = {
//                   viewModel._showBookmarkListDialog.value = false
//                   viewModel._showFontSlider.value = !showFontSlider
//                    viewModel._showThemeSelector.value = false
//                })
//            {
//                Icon(
//                    painter = painterResource(id = R.drawable.font_size),
//                    contentDescription = "Adjust font size",
//                )
//            }
//            IconButton(
//                modifier = Modifier.weight(1.5f),
//                onClick = {
//                viewModel._showBookmarkListDialog.value = !showBookmarkListDialog
//                viewModel._showThemeSelector.value = false
//                viewModel._showFontSlider.value = false
//            }) {
//                Icon(
//
//                    painter = painterResource(id = R.drawable.bookmark1),
//                    contentDescription = "Adjust font size",
//                )
//            }
//
////                            CustomButton()
//
//            Box(
//                modifier = Modifier
//                    .weight(1.5f)
//                    .background(
//                        color = MaterialTheme.colorScheme.onSecondaryContainer, // Customize background color
//                        shape = RoundedCornerShape(16.dp)
//                    )
//                    .padding(8.dp),
//                contentAlignment = Alignment.Center
//
//            ) {
//                Text(
//                    text = pageNumber.toString(),
//                    color = MaterialTheme.colorScheme.secondaryContainer,
//                    fontSize = 20.sp
//                )
//            }
//
//            IconButton(
//                modifier = Modifier.weight(1.5f),
//                onClick = {}
//            ){
//                Icon(
//                    painter = painterResource(id = R.drawable.settings),
//                    contentDescription = "Settings"
//                )
//            }
//
//            Box(
//                modifier = Modifier.weight(1.5f),
//                contentAlignment = Alignment.TopCenter // Align DropdownMenu to the top
//            ) {
//                IconButton(
//                    onClick = {
//                        viewModel._showThemeSelector.value = !showThemeSelector
//                        viewModel._showFontSlider.value = false
//                        viewModel._showBookmarkListDialog.value = false
//                    }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.themes),
//                        contentDescription = "Choose theme"
//                    )
//                }
//            }
//        }
//    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: @Composable () -> Unit,
    var showDialog: Boolean,
    val onClick: () -> Unit = {}
)