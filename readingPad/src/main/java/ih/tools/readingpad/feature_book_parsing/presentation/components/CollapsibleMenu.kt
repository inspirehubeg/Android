package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CollapsibleMenu(
    modifier: Modifier,
    selected: Boolean,
    title: String,
    onItemSelected: () -> Unit,

    ) {
    var expanded by remember { mutableStateOf(selected) }

    Column(modifier = modifier) {
        TextButton(modifier = Modifier
            .height(50.dp)
            .padding(start = 8.dp, top = 8.dp, end = 16.dp, bottom = 4.dp),
            onClick = {
                expanded = !expanded
                onItemSelected()
            }
        ) {
            Text(text = title)
        }
//        Text(text = title,
//            modifier = Modifier
//                .height(50.dp)
//                .clickable {
//                    expanded = !expanded
//                    onItemSelected()
//                }
//                .padding(8.dp))
    }
}

@Composable
fun MenuContent(
    content: @Composable () -> Unit
) {
    content()
}


