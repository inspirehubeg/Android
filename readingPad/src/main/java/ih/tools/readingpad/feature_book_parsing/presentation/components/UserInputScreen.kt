package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel

@Composable
fun UserInputScreen(viewModel: BookContentViewModel, height: Dp, offset: Dp){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .offset(y = offset)
            .animateContentSize()
            .background(Color.White)
    ) {
        Column {
            // Back button
            IconButton(onClick = { viewModel.toggleShowUserInputPage() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            // Content of the pop-up screen
            // ...
        }
    }

}