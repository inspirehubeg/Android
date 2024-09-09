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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingPadTopBar(
    navController: NavController,
    viewModel: BookContentViewModel
) {
    val pinnedTopBar by viewModel.pinnedTopBar.collectAsState()
    var topAppBarHeight by remember { mutableStateOf(48.dp) } // Initial height
    var showSearchBar by remember { mutableStateOf(false) }
    topAppBarHeight =
        if (showSearchBar) 64.dp else 48.dp // Expands the height when the search opens
    var searchText by remember { mutableStateOf("") }
    val isDrawerOpen by viewModel.isDrawerOpen.collectAsState()

    val showCustomSelectionMenu by viewModel.showCustomSelectionMenu.collectAsState()

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
                }else if (showCustomSelectionMenu){
                    CustomSelectionMenu(viewModel = viewModel)
                }
                else {
                    Text(text = "") // Or maybe chapter name
                }
            },

            navigationIcon = {
                if (!showSearchBar && !showCustomSelectionMenu) { // Only show actions when search bar is closed
                    IconButton(onClick = {
                        if (isDrawerOpen) {
                            viewModel.closeDrawer()
                        } else viewModel.openDrawer()
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
                        viewModel.setPinnedTopBar(!pinnedTopBar)
                    }) {
                        Icon(Icons.Default.PushPin, contentDescription = "")
                    }
                }

            }
        )
    }
}
