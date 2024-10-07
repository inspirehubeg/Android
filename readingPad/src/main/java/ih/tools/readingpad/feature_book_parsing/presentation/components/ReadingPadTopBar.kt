package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel
import ih.tools.readingpad.util.PermissionRequester


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingPadTopBar(
    navController: NavController,
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel
) {
    val uiSettings by uiStateViewModel.uiSettings.collectAsState()
    val pinnedTopBar = uiSettings.pinnedTopBar
    //val pinnedTopBar by viewModel.pinnedTopBar.collectAsState()
    val topAppBarHeight by uiStateViewModel.topBarHeight.collectAsState() // Initial height
    val showSearchBar = uiSettings.showSearchBar
    val density = LocalDensity.current
   uiStateViewModel.setTopBarHeight(
        if (uiSettings.showSearchBar) {
            with(density) { 64.dp.toPx() }
        } else {
            with(density) { 48.dp.toPx() }
        } // Expands the height when the search opens
   )
    var searchText by remember { mutableStateOf("") }
    val isDrawerOpen by uiStateViewModel.drawerState.collectAsState()

    val showCustomSelectionMenu by uiStateViewModel.showCustomSelectionMenu.collectAsState()
    val context = LocalContext.current
    val permissionRequester = remember { context as PermissionRequester }
    Surface(
        modifier = Modifier.height(
            with(density) { topAppBarHeight.toDp() })
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            title = {
                // this is the middle part of the top bar.
                // if search is expanded then it will occupy this part and both the action and navigation icons will disappear
                if (showSearchBar) {
                    TextField(
                        colors = TextFieldDefaults.colors().copy(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),

                        singleLine = true,
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        placeholder = {
                            Text(
                                stringResource(R.string.search_placeholder),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        },

                        trailingIcon = {
                            IconButton(onClick = {
                                uiStateViewModel.updateUISettings(
                                    uiSettings.copy(
                                        showSearchBar = false
                                    )
                                )
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        },
                        leadingIcon = {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = stringResource(R.string.clear),
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        },
                    )
                } else if (showCustomSelectionMenu) {
                    TopBarSelectionMenu(viewModel = viewModel, uiStateViewModel = uiStateViewModel)
                } else {
                    Text(text = "") // Or maybe chapter name
                }
            },

            navigationIcon = {
                if (!showSearchBar && !showCustomSelectionMenu) { // Only show actions when search bar is closed
                    IconButton(onClick = {
                        uiStateViewModel.setDrawerState(DrawerState(DrawerValue.Open))
//                        uiStateViewModel.updateUISettings(
//                            uiSettings.copy(
//                                isDrawerOpen = !isDrawerOpen
//                            )
//                        )
                    })
                    {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            },
            actions = {
                if (!showSearchBar && !showCustomSelectionMenu) { // Only show actions when search bar is closed
                    IconButton(onClick = {
                        uiStateViewModel.updateUISettings(
                            uiSettings.copy(
                                showSearchBar = true
                            )
                        )
                    }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                    IconButton(onClick = {
                       uiStateViewModel.setPinnedTopBar(!pinnedTopBar)
                    }) {
                        Icon(Icons.Default.PushPin, contentDescription = "")
                    }
                    MoreSubMenu(uiStateViewModel = uiStateViewModel, viewModel = viewModel, permissionRequester)

                }
            }
        )
    }
}

data class HighlightParagraph(
    val chapterName: String,
    val pageNumber: String,
    val highlightText: String,

    )