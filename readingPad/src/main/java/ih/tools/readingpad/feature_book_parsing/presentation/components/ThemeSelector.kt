package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.theme.DarkBackground
import ih.tools.readingpad.ui.theme.DarkGray
import ih.tools.readingpad.ui.theme.LightBackground
import ih.tools.readingpad.ui.theme.LightGray

@Composable
fun ThemeSelectorMenu(
    viewModel: BookContentViewModel,
) {
    val isDarkTheme = viewModel.darkTheme.collectAsState().value
    val onDismissRequest = { viewModel.setShowThemeSelector(false) }
    //outer transparent box that allows the custom positioning of the theme menu
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .clickable(onClick = onDismissRequest)
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .align(Alignment.BottomEnd)
        ) {
            Row (
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp)
            ){
                //dark mode
                ThemeSelectorItem(
                    backgroundColor = DarkBackground.toArgb(),
                    textColor = LightGray.toArgb(),
                    isSelected = isDarkTheme,
                    onClick = {
                        viewModel.setDarkTheme(true, LightGray.toArgb(), backgroundColor = DarkBackground.toArgb())
                        onDismissRequest()
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                //Light mode
                ThemeSelectorItem(
                    backgroundColor = LightBackground.toArgb(),
                    textColor = DarkGray.toArgb(),
                    isSelected = !isDarkTheme,
                    onClick = {
                        viewModel.setDarkTheme(false, DarkGray.toArgb(), backgroundColor = LightBackground.toArgb())
                        onDismissRequest()
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Card (
                    elevation = CardDefaults.cardElevation(8.dp),
                ){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(65.dp)
                            .background(color = MaterialTheme.colorScheme.surface)
                    ) {
                        IconButton(onClick = {
                            viewModel.setShowThemeSelector(false)
                            viewModel.setShowCustomThemePage(true)
                        }) {
                           Icon (
                               Icons.Default.Add , contentDescription = stringResource(R.string.add_custom_theme)
                           )
                        }
                    }
                }
            }
        }
    }
}

// single Theme menu item that shows how a theme look
@Composable
fun ThemeSelectorItem(
    backgroundColor: Int,
    textColor: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card (
        elevation = CardDefaults.cardElevation(8.dp),
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(
                    width = if (isSelected) 4.dp else 0.dp,
                    color = if (isSelected) Color.Red else Color.Transparent
                )
                .size(65.dp)
                .background(color = Color(backgroundColor))
                .clickable { onClick() }
        ) {
            Text(text = stringResource(R.string.theme_item_string), color = Color(textColor), fontSize = 35.sp)
        }
    }

}


@Composable
@Preview
fun ThemeSelectorItemPreview() {
    val backgroundColor = DarkBackground.toArgb()
    val textColor = LightGray.toArgb()
    ThemeSelectorItem(backgroundColor, textColor)
}