package ih.tools.readingpad.feature_note.presentation

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
fun AddNoteDialog(
    viewModel: BookContentViewModel,

    ) {
    var noteText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val onDismiss = {
        viewModel.setShowAddNoteDialog(false)
        viewModel.setNoteClickEvent(null)
    }
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onDismissRequest = { onDismiss() },
        text = {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Note")
                Spacer(modifier = Modifier.width(4.dp))
                TextField(
                    singleLine = true,
                    placeholder = { Text("Add your note") },
                    value = noteText,
                    onValueChange = { noteText = it }
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
                        if (viewModel.textView.value != null) {
                            viewModel.addNote(
                                noteText = noteText.ifBlank {
                                    context.getString(R.string.default_new_bookmark_name)
                                },
                                start = viewModel.noteStart.value,
                                end = viewModel.noteEnd.value,
                                textView = viewModel.textView.value!!
                            )
                            Log.d(
                                "new", "viewModel.textView is not null page ${viewModel.textView.value!!.pageNumber}"
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
}
