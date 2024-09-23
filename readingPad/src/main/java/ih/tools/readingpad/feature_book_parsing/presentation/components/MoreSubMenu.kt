package ih.tools.readingpad.feature_book_parsing.presentation.components

import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel
import ih.tools.readingpad.util.createWordDoc
import java.io.File

@Composable
fun MoreSubMenu(
    uiStateViewModel: UIStateViewModel,
    viewModel: BookContentViewModel,
){
    var subMenuExpanded by remember { mutableStateOf(false) }

    val uiSettings by uiStateViewModel.uiSettings.collectAsState()
    val keepScreenOn = uiSettings.keepScreenOn
    val showHighlightsBookmarks = uiSettings.showHighlightsBookmarks
    val context = LocalContext.current
    val externalStoragePath = context.getExternalFilesDir(null)?.absolutePath


    LaunchedEffect(uiStateViewModel) {
        uiStateViewModel.uiSettings.collect { settings ->
            val window = (context as Activity).window
            if (settings.keepScreenOn) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    Box() {
        IconButton(onClick = { subMenuExpanded = true }) {
            Icon(Icons.Filled.MoreVert, contentDescription = "More")
        }
        DropdownMenu(
            modifier = Modifier
                //.background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(4.dp),
            expanded = subMenuExpanded,
            onDismissRequest = { subMenuExpanded = false }
        ) {
            DropdownMenuItem(text = {
                Text(text = "Keep screen on")
            }, trailingIcon = {
                Checkbox(
                    checked = keepScreenOn,
                    onCheckedChange = {
                        //viewModel.setKeepScreenOn(it)
                        uiStateViewModel.updateUISettings(
                            uiSettings.copy(keepScreenOn = it)
                        )
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        ),
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }, onClick = {

            })
            DropdownMenuItem(text = {
                Text(text = "Show bookmarks and notes")
            }, trailingIcon = {
                Checkbox(
                    checked = showHighlightsBookmarks,
                    onCheckedChange = {
                        //viewModel.setShowHighlightsBookmarks(it)
                        uiStateViewModel.setShowHighlightsBookmarks(!showHighlightsBookmarks)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        ),
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }, onClick = {

            })
            DropdownMenuItem(text = {
                Text(text = "Export Highlights")
            }, trailingIcon = {
                Icon(Icons.Filled.DocumentScanner, contentDescription = "")
            },
                onClick = {
                    val fileName =
                        "${viewModel.state.value.bookTitle} Highlights.docx"
                    val text: MutableList<HighlightParagraph> = mutableListOf()
                    for (i in viewModel.state.value.bookHighlights) {
                        text.add(
                            HighlightParagraph(
                                chapterName = "Chapter: ${i.chapterNumber}",
                                pageNumber = "Page: ${i.pageNumber}",
                                highlightText = i.text
                            )
                        )
                    }
                    val file = File(externalStoragePath, fileName)

                    createWordDoc(
                        text,
                        file.absolutePath,
                        bookName = viewModel.state.value.bookTitle
                    )
                    Toast.makeText(
                        context,
                        "File saved to ${file.absolutePath}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}