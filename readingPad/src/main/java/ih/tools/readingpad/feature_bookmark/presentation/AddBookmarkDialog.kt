package ih.tools.readingpad.feature_bookmark.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel

@Composable
fun AddBookmarkDialog(
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel

    ) {
    //var bookmarkName by remember { mutableStateOf(viewModel.bookmarkName.value) }
    // Update state observation
    val bookmarkName by uiStateViewModel.bookmarkName.collectAsState()
    val bookmarkStart by uiStateViewModel.bookmarkStart.collectAsState()
    val bookmarkEnd by uiStateViewModel.bookmarkEnd.collectAsState()
    val bookmarkPageNumber by uiStateViewModel.bookmarkPageNumber.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val onDismiss = {
//        viewModel.setShowAddBookmarkDialog(false)
//        viewModel.setBookmarkClickEvent(null)
        uiStateViewModel.showDialog(null) // Use uiStateViewModel to close dialog
        uiStateViewModel.setBookmarkClickEvent(null) // Use uiStateViewModel to reset event
    }
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onDismissRequest = { onDismiss() },
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add a new Bookmark",
                    textAlign = TextAlign.Center, // Center align text
                    modifier = Modifier.align(Alignment.Center) // Center within the Box)
                )
            }
        },
        text = {
            TextField(
                singleLine = true,
                placeholder = { Text(stringResource(R.string.bookmark_placeholder)) },
                value = bookmarkName,
                onValueChange = { if (it.length <= 25) uiStateViewModel.setBookmarkName(it) },
                modifier = Modifier.focusRequester(focusRequester)
            )

        },
        confirmButton = {
            Row(
                Modifier.fillMaxWidth(),
                // verticalAlignment = Alignment.CenterVertically,
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
                        if (viewModel.textView.value != null) {
                            viewModel.addBookmark(
                                bookmarkName = bookmarkName.ifBlank {
                                    context.getString(R.string.default_new_bookmark_name)
                                },
                                textView = viewModel.textView.value!!,
                                start = bookmarkStart,
                                end = bookmarkEnd,
                                pageNumber = bookmarkPageNumber
                            )
                            Log.d(
                                "new",
                                "viewModel.textView is not null page ${viewModel.textView.value!!.pageNumber}"
                            )
                        } else {
                            Log.e("new", "viewModel.textView is null")
                        }
                        onDismiss()
                    }
                ) {
                    Icon(Icons.Default.Done, contentDescription = stringResource(R.string.done))
                    //Text("OK")
                }
            }
        },
    )
    LaunchedEffect(Unit) { // Launch effect when dialog appears
        focusRequester.requestFocus() // Request focus for the TextField
    }
}
