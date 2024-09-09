package ih.tools.readingpad.feature_book_parsing.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Slider
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
import ih.tools.readingpad.ui.theme.Green
import ih.tools.readingpad.ui.theme.LightBackground

@Composable
fun ReadingPadBottomBar(
    viewModel: BookContentViewModel,
    navController: NavController
) {
    val showFontSlider by viewModel.showFontSlider.collectAsState()
    val showThemeSelector by viewModel.showThemeSelector.collectAsState()
    val showBookmarkListDialog by viewModel.showBookmarkListDialog.collectAsState()
    val showPageNumberDialog by viewModel.showPageNumberDialog.collectAsState()
    val showPagesSlider by viewModel.showPagesSlider.collectAsState()
    val pageNumber by viewModel.pageNumber
    val numberOfPages = viewModel.state.value.numberOfPages
    Log.d("number of pages", "$numberOfPages")
    val oneFingerScroll by viewModel.oneFingerScroll.collectAsState()
    // the list of bottom bar items, they are displayed in the same order
    val itemList: List<NavigationItem> = listOf(
        // this is just a place holder to be changed later with a working icon


        NavigationItem(
            title = stringResource(R.string.bookmarks),
            content = {
                val currentPage = viewModel.pageNumber.value
                val totalPages = 5  /*viewModel.state.value.numberOfPages*/
                val progress = if (totalPages > 0) currentPage.toFloat() / totalPages else 0f

                //LinearProgressIndicator(progress = { progress })
                CircularProgressIndicator(
                    progress = { progress },
                    strokeWidth = 20.dp, // Adjust stroke width as needed
                    color = Green // Customize color as needed
                )
                //Icon(Icons.Default.Bookmarks, contentDescription = stringResource(R.string.bookmarks))
            },
            showDialog = showBookmarkListDialog,
            onClick = {
                viewModel.setShowPagesSlider(!showPagesSlider)
                viewModel.setShowThemeSelector(false)
                viewModel.setShowFontSlider(false)
                viewModel.setShowPageNumberDialog(false)
                viewModel.setShowCustomThemePage(false)
            }
        ),
        NavigationItem(
            title = stringResource(R.string.settings),
            content = {
                //Icon(Icons.Default.Dashboard, "")
            },
            showDialog = false,
            onClick = {
                // viewModel.toggleShowUserInputPage()
                //viewModel.setTopBarVisibility(false)
                // viewModel.setOneFingerScroll(!oneFingerScroll)
                //viewModel.setVerticalScroll(!verticalScroll)
                // viewModel.setShowCustomThemePage(true)
            }
        ),
        NavigationItem(
            title = stringResource(R.string.page_number),
            content = {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer, // Customize background color
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp),
                ) {


//                        Box(Modifier.padding(end = 4.dp)){
                    Text(
                        text = pageNumber.toString(),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        fontSize = 20.sp
                    )
                }


            },
            showDialog = showPageNumberDialog,
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
            content = {
                Icon(
                    Icons.Default.FormatSize,
                    contentDescription = stringResource(R.string.adjust_font_size)
                )
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
            content = {
                Icon(
                    Icons.Default.ColorLens,
                    contentDescription = stringResource(R.string.choose_a_theme)
                )
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
            //when the progress clicked
            if (showPagesSlider) {
                var number = pageNumber.toFloat()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                        .background(LightBackground, RoundedCornerShape(10.dp)),
                ) {
                    IconButton(onClick = { viewModel.setShowPagesSlider(false) }) {
                        Icon(Icons.Default.Cancel, contentDescription ="" )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Slider(
                        modifier = Modifier.padding(4.dp),
                        value = number,
                        onValueChange = {
                            number = it
                            viewModel.navigateToPage(
                                number.toInt() - 1,
                                lazyListState = viewModel.lazyListState
                            )
                        },
                        valueRange = 1f..5f,
                        steps = 5
                    )
                }
            } else {

                itemList.forEach { navigationItem ->
                    NavigationBarItem(
                        modifier = Modifier.padding(4.dp),
                        selected = navigationItem.showDialog,
                        onClick = { navigationItem.onClick() },
                        icon = navigationItem.content,
                    )
                }
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
    //val modifier: Modifier,
    val content: @Composable () -> Unit,
    var showDialog: Boolean,
    val onClick: () -> Unit = {}
)