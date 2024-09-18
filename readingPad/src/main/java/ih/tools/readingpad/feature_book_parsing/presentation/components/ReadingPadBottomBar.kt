package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel
import ih.tools.readingpad.ui.theme.Green

@Composable
fun ReadingPadBottomBar(
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel = hiltViewModel(),
    navController: NavController
) {

    val pageNumber by viewModel.pageNumber
    val showFontSlider = uiStateViewModel.currentDialog.collectAsState()
        .value == UIStateViewModel.DialogType.FontSlider
    val showThemeSelector = uiStateViewModel.currentDialog.collectAsState()
        .value == UIStateViewModel.DialogType.ThemeSelector
    val showBookmarkListDialog = uiStateViewModel.currentDialog.collectAsState()
        .value == UIStateViewModel.DialogType.BookmarkList
    val showBrightnessDialog = uiStateViewModel.currentDialog.collectAsState()
        .value == UIStateViewModel.DialogType.Brightness
    val showPageNumberDialog = uiStateViewModel.currentDialog.collectAsState()
        .value == UIStateViewModel.DialogType.PageNumber
    val showPagesSlider = uiStateViewModel.currentDialog.collectAsState()
        .value == UIStateViewModel.DialogType.PagesSlider
    // the list of bottom bar items, they are displayed in the same order
    val itemList: List<NavigationItem> = listOf(

        NavigationItem(
            title = "Progress",
            content = {
                val currentPage = viewModel.pageNumber.value
                val totalPages = 5  /*viewModel.state.value.numberOfPages*/
                val progress = if (totalPages > 0) currentPage.toFloat() / totalPages else 0f

                //LinearProgressIndicator(progress = { progress })
                CircularProgressIndicator(
                    progress = { progress },
                    trackColor = Color.Gray,
                    strokeWidth = 10.dp, // Adjust stroke width as needed
                    color = Green // Customize color as needed
                )
                //Icon(Icons.Default.Bookmarks, contentDescription = stringResource(R.string.bookmarks))
            },
            showDialog = showPagesSlider,
            onClick = {
                uiStateViewModel.showDialog(
                    if (showPagesSlider) null
                    else UIStateViewModel.DialogType.PagesSlider
                )
               // uiStateViewModel.showScreen(null)
//                viewModel.setShowPagesSlider(!showPagesSlider)
//                viewModel.setShowThemeSelector(false)
//                viewModel.setShowFontSlider(false)
//                viewModel.setShowPageNumberDialog(false)
//                viewModel.setShowCustomThemePage(false)
//                viewModel.setTopBarVisibility(true)
            }
        ),
        NavigationItem(
            title = "Brightness",
            content = {
                Icon(Icons.Default.BrightnessMedium, "Brightness")
            },
            showDialog = false,
            onClick = {
                uiStateViewModel.showDialog(
                    if (showBrightnessDialog) null
                    else UIStateViewModel.DialogType.Brightness
                )
                //uiStateViewModel.showScreen(null)
//                viewModel.setShowBrightnessDialog(!showBrightnessDialog)
//                viewModel.setShowBookmarkListDialog(false)
//                viewModel.setShowThemeSelector(false)
//                viewModel.setShowFontSlider(false)
//                viewModel.setShowCustomThemePage(false)
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
                uiStateViewModel.showDialog(
                    if (showPageNumberDialog) null
                    else UIStateViewModel.DialogType.PageNumber
                )
                //uiStateViewModel.showScreen(null)

//                viewModel.setShowPageNumberDialog(!showPageNumberDialog)
//                viewModel.setShowBookmarkListDialog(false)
//                viewModel.setShowThemeSelector(false)
//                viewModel.setShowFontSlider(false)
//                viewModel.setShowCustomThemePage(false)

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
                uiStateViewModel.showDialog(
                    if (showFontSlider) null
                    else UIStateViewModel.DialogType.FontSlider
                )
               // uiStateViewModel.showScreen(null)
//                viewModel.setShowFontSlider(!showFontSlider)
//                viewModel.setShowThemeSelector(false)
//                viewModel.setShowBookmarkListDialog(false)
//                viewModel.setShowPageNumberDialog(false)
//                viewModel.setShowCustomThemePage(false)
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
                uiStateViewModel.showDialog(
                    if (showThemeSelector) null
                    else UIStateViewModel.DialogType.ThemeSelector
                )
                //uiStateViewModel.showScreen(null)
//                viewModel.setShowThemeSelector(!showThemeSelector)
//                viewModel.setShowFontSlider(false)
//                viewModel.setShowBookmarkListDialog(false)
//                viewModel.setShowPageNumberDialog(false)
//                viewModel.setShowCustomThemePage(false)

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
                PagesSlider(viewModel, uiStateViewModel)
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
}

data class NavigationItem(
    val title: String,
    val content: @Composable () -> Unit,
    var showDialog: Boolean,
    val onClick: () -> Unit = {}
)