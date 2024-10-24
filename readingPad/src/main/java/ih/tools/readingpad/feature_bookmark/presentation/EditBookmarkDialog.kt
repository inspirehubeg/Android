package ih.tools.readingpad.feature_bookmark.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel

@Composable
fun EditBookmarkDialog(
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel
) {
   // var bookmarkName by remember { mutableStateOf(viewModel.bookmarkClickEvent.value?.bookmarkName) }
    val bookmarkName = uiStateViewModel.bookmarkClickEvent.collectAsState().value?.bookmarkName
    val bookmarkId = uiStateViewModel.bookmarkClickEvent.collectAsState().value?.id
    //val bookmarkId by remember { mutableStateOf(viewModel.bookmarkClickEvent.value?.id) }
    val context = LocalContext.current

    val onDismiss = {
//        viewModel.setShowEditBookmarkDialog(false)
//        viewModel.setBookmarkClickEvent(null)
        uiStateViewModel.showDialog(null)
        uiStateViewModel.setBookmarkClickEvent(null)
    }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onDismissRequest = {
            onDismiss()
        },

        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Edit Bookmark",
                    textAlign = TextAlign.Center, // Center align text
                    modifier = Modifier.align(Alignment.Center) // Center within the Box)
                )
            }
        },
        text = {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
//                Text(text = stringResource(R.string.bookmark_dialog_name))
//                Spacer(modifier = Modifier.width(4.dp))
                TextField(
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.bookmark_placeholder)) },
                    value = bookmarkName!!,
                    onValueChange = {if (it.length <= 25) uiStateViewModel.setBookmarkName(it) }
                )
            }
        },
        confirmButton = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    modifier = Modifier,
                    onClick = {
                        onDismiss()
                    }) {
                    // Icon(Icons.Default.Close, contentDescription = "cancel")
                    Text(stringResource(R.string.cancel))
                }
                Spacer(modifier = Modifier.weight(2f))

                FilledIconButton(
                    modifier = Modifier,
                    onClick = {
                        viewModel.removeBookmarkById(bookmarkId ?: 0)
                        onDismiss()
                    }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                    //Text("Delete")
                }

                Spacer(modifier = Modifier.width(4.dp))


                FilledIconButton(
                    modifier = Modifier,
                    onClick = {
                        if (viewModel.bookmarkClickableSpans[bookmarkId] != null) {
//                            if (viewModel.checkIfBookmarkExists(bookmarkId ?: 0)){
                            Log.d("new", "bookmark exists")
                            viewModel.updateBookmarkTitle(
                                bookmarkId!!,
                                newTitle = bookmarkName!!.ifBlank {
                                    context.getString(R.string.default_new_bookmark_name)
                                }
                            )
                            uiStateViewModel.setBookmarkClickEvent(null)
                        }
                        onDismiss()
                    }
                ) {
                    Icon(Icons.Default.Done, contentDescription = "Done")
                    //Text("OK")
                }
            }
        },
    )
}
