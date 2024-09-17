package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel
import ih.tools.readingpad.ui.theme.LightBackground

@Composable
fun PagesSlider(viewModel: BookContentViewModel, uiStateViewModel: UIStateViewModel) {
    var number = viewModel.pageNumber.value.toFloat()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .background(LightBackground, RoundedCornerShape(10.dp)),
    ) {
        IconButton(onClick = {
            uiStateViewModel.showDialog(null)
        }) {
            Icon(Icons.Default.Cancel, contentDescription = "")
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
}