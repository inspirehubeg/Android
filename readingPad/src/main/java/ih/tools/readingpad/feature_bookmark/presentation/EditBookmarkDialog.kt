package ih.tools.readingpad.feature_bookmark.presentation

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel

@Composable
fun EditBookmarkDialog(
    viewModel: BookContentViewModel,
) {
    var bookmarkName by remember { mutableStateOf(viewModel.bookmarkClickEvent.value?.bookmarkName) }
    val bookmarkId by remember { mutableStateOf(viewModel.bookmarkClickEvent.value?.id) }
    val context = LocalContext.current

    val onDismiss = {
        viewModel._showEditBookmarkDialog.value = false
        viewModel._bookmarkClickEvent.value = null
    }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onDismissRequest = {
            onDismiss()
        },

        //title = { Text("Enter Bookmark Name") },
        text = {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.bookmark_dialog_name))
                Spacer(modifier = Modifier.width(4.dp))
                TextField(
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.bookmark_placeholder))  },
                    value = bookmarkName!!,
                    onValueChange = { bookmarkName = it }
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
                        if (viewModel.bookmarkSpans[bookmarkId] != null) {
//                            if (viewModel.checkIfBookmarkExists(bookmarkId ?: 0)){
                            Log.d("new", "bookmark exists")
                            viewModel.updateBookmarkTitle(
                                bookmarkId!!,
                                newTitle = bookmarkName!!.ifBlank {
                                    context.getString(R.string.default_new_bookmark_name)
                                }
                            )
                            viewModel._bookmarkClickEvent.value = null
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
