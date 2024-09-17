package ih.tools.readingpad.feature_note.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun AddNoteDialog(
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel
    ) {
   // var noteText by remember { mutableStateOf("") }
    val noteText by uiStateViewModel.noteText.collectAsState()
    val noteStart by uiStateViewModel.noteStart.collectAsState()
    val noteEnd by uiStateViewModel.noteEnd.collectAsState()
    val notePageNumber by uiStateViewModel.notePageNumber.collectAsState()

    val context = LocalContext.current
    val onDismiss = {
        uiStateViewModel.showDialog(null)
        uiStateViewModel.setNoteClickEvent(null)
        uiStateViewModel.setNoteText("")
//        viewModel.setShowAddNoteDialog(false)
//        viewModel.setNoteClickEvent(null)
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
                    text = "Add a Note",
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
                    modifier = Modifier.fillMaxSize(),
                    placeholder = { Text("Add your note") },
                    value = noteText,
                    onValueChange = {uiStateViewModel.setNoteText(it) },
                    )
            }
        },
        confirmButton = {
            Row(
                Modifier.fillMaxWidth(),
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
                                start = noteStart,
                                end = noteEnd,
                                pageNumber = notePageNumber,
                                textView = viewModel.textView.value!!
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
}
