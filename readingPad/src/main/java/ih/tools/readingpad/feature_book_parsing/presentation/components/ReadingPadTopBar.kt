package ih.tools.readingpad.feature_book_parsing.presentation.components

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel


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
    var topAppBarHeight by remember { mutableStateOf(48.dp) } // Initial height
    var showSearchBar by remember { mutableStateOf(false) }
    topAppBarHeight =
        if (showSearchBar) 64.dp else 48.dp // Expands the height when the search opens
    var searchText by remember { mutableStateOf("") }

    //val isDrawerOpen by viewModel.isDrawerOpen.collectAsState()
    val isDrawerOpen = uiSettings.isDrawerOpen

    var subMenuExpanded by remember { mutableStateOf(false) }
    val showCustomSelectionMenu by uiStateViewModel.showCustomSelectionMenu.collectAsState()
    //val keepScreenOn by viewModel.keepScreenOn.collectAsState()
    //val showHighlightsBookmarks by viewModel.showHighlightsBookmarks.collectAsState()
    val keepScreenOn = uiSettings.keepScreenOn
    val showHighlightsBookmarks = uiSettings.showHighlightsBookmarks

    val context = LocalContext.current
    LaunchedEffect(uiStateViewModel) {
//        viewModel.keepScreenOnEvent.collect { keepScreenOn ->
//            val window = (context as Activity).window
//            if (keepScreenOn) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//            } else {
//                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//            }
//        }
        uiStateViewModel.uiSettings.collect { settings ->
            val window = (context as Activity).window
            if (settings.keepScreenOn) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    Surface(modifier = Modifier.height(topAppBarHeight)) {
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
                            IconButton(onClick = { showSearchBar = false }) {
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
                    CustomSelectionMenu(viewModel = viewModel, uiStateViewModel = uiStateViewModel)
                } else {
                    Text(text = "") // Or maybe chapter name
                }
            },

            navigationIcon = {
                if (!showSearchBar && !showCustomSelectionMenu) { // Only show actions when search bar is closed
                    IconButton(onClick = {
                        uiStateViewModel.updateUISettings(
                            uiSettings.copy(
                                isDrawerOpen = !uiSettings.isDrawerOpen
                            )
                        )
//                        if (isDrawerOpen) {
//                            viewModel.closeDrawer()
//                        } else {
//                            viewModel.openDrawer()
//                        }
                    })
                    {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            },
            actions = {
                if (!showSearchBar && !showCustomSelectionMenu) { // Only show actions when search bar is closed
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                    IconButton(onClick = {
                        // viewModel.setPinnedTopBar(!pinnedTopBar)
                        uiStateViewModel.setPinnedTopBar(!pinnedTopBar)
                    }) {
                        Icon(Icons.Default.PushPin, contentDescription = "")
                    }

                    Box() {
                        IconButton(onClick = { subMenuExpanded = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(
                            modifier = Modifier
                                //.background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(4.dp),
                            expanded = subMenuExpanded,
                            onDismissRequest = { subMenuExpanded = false }
                        ) {
                            DropdownMenuItem(text = {
                                Text(text = "Keep screen on")
                            }, trailingIcon = {
                                Checkbox(
                                    checked = keepScreenOn,
                                    onCheckedChange = {
                                        //viewModel.setKeepScreenOn(it)
                                        uiStateViewModel.updateUISettings(
                                            uiSettings.copy(keepScreenOn = it)
                                        )
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                        uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.6f
                                        ),
                                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }, onClick = {

                            })
                            DropdownMenuItem(text = {
                                Text(text = "Show bookmarks and notes")
                            }, trailingIcon = {
                                Checkbox(
                                    checked = showHighlightsBookmarks,
                                    onCheckedChange = {
                                        //viewModel.setShowHighlightsBookmarks(it)
                                        uiStateViewModel.setShowHighlightsBookmarks(!showHighlightsBookmarks)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                        uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.6f
                                        ),
                                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }, onClick = {

                            })

                        }
                    }
                }


            }
        )
    }
}
