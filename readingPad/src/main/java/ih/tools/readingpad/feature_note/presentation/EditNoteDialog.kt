package ih.tools.readingpad.feature_note.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun EditNoteDialog(
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel
) {
   // var noteText by remember { mutableStateOf(viewModel.noteClickEvent.value?.noteText) }
    val noteText = uiStateViewModel.noteClickEvent.collectAsState().value?.noteText
    //val noteId by remember { mutableStateOf(viewModel.noteClickEvent.value?.id) }
    val noteId = uiStateViewModel.noteClickEvent.collectAsState().value?.id
    val context = LocalContext.current

    val onDismiss = {
//        viewModel.setShowEditNoteDialog(false)
//        viewModel.setNoteClickEvent(null)
        uiStateViewModel.showDialog(null)
        uiStateViewModel.setNoteClickEvent(null)
        uiStateViewModel.setNoteText("")
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
                    text = "Edit Note",
                    textAlign = TextAlign.Center, // Center align text
                    modifier = Modifier.align(Alignment.Center) // Center within the Box)
                )
            }
        },
        text = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = noteText!!,
                    onValueChange = { uiStateViewModel.setNoteText(it) },
                    modifier = Modifier.fillMaxSize()
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
                        viewModel.removeNoteById(noteId ?: 0)
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
                        if (viewModel.noteClickableSpans[noteId] != null) {
                            if (noteText != null) {
                                viewModel.updateNoteText(
                                    noteId!!,
                                    newText = noteText.ifBlank {
                                        context.getString(R.string.default_new_bookmark_name)
                                    }
                                )
                            }
                            uiStateViewModel.setNoteClickEvent(null)
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
