package alexschool.bookreader.ui

import alexschool.bookreader.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getBookInfo
import ih.tools.readingpad.ui.theme.Beige
import ih.tools.readingpad.ui.theme.Brown

@Composable
fun HomeScreen (navController: NavController) {

    val context = LocalContext.current
    val bookInfo = getBookInfo(context)

//
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        //contentAlignment = Alignment.Center
    ){
        IconButton(
            modifier = Modifier.align(Alignment.BottomStart),
            onClick = {
            navController.navigate(Screens.SettingsScreen.route)
        }) {
            Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
        }
        Button(
            modifier = Modifier.align(Alignment.Center),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Beige,
                contentColor = Brown
            ),
            onClick = {
                navController.navigate(Screens.BookContentScreen.route + "?bookId=${bookInfo.id}")
            }) {
            Text(text = stringResource(R.string.book_content))
        }
    }


}