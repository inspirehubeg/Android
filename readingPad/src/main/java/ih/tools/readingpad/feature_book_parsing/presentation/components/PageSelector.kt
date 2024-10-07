package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel




@Composable
fun PageSelector(viewModel: BookContentViewModel, listState: LazyListState, uiStateViewModel: UIStateViewModel) {
    var number: String by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(true) }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .clickable(onClick = {
                uiStateViewModel.showDialog(null)
            })
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                TextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(2f),
                    colors = TextFieldDefaults.colors().copy(
                        errorTextColor = Color.Red,
                        errorContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    isError = isError,
                    value = number,
                    onValueChange = { newValue ->
                        number = newValue.filter { it.isDigit() }
                        if (number.isNotEmpty() &&
                            number.toInt() in 1..listState.layoutInfo.totalItemsCount
                        ) {
                            isError = false
                        } else {
                            isError = true
                        }
                    }
                )
                Button(
                    enabled = !isError,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.navigateToPage(number.toInt() - 1, listState)
                        uiStateViewModel.showDialog(null)
                        uiStateViewModel.toggleTopBar(false)
                    }
                ) {
                    Text("Go")
                }
            }

        }
    }
}