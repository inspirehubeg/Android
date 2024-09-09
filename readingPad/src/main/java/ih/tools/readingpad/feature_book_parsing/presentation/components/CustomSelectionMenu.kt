package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkClickableSpan
import ih.tools.readingpad.ui.theme.BookmarkDialogBrown
import ih.tools.readingpad.ui.theme.HighlightCheeseOrange
import ih.tools.readingpad.ui.theme.HighlightFrenchLime
import ih.tools.readingpad.ui.theme.HighlightLaserLemon
import ih.tools.readingpad.ui.theme.HighlightMayaBlue
import ih.tools.readingpad.ui.theme.HighlightUltraRed
import ih.tools.readingpad.util.IHBackgroundSpan
import ih.tools.readingpad.util.IHNoteSpan

@Composable
fun CustomSelectionMenu(viewModel: BookContentViewModel) {
    val showColorsMenu = remember { mutableStateOf(false) }
    val preferredHighlightColor by viewModel.preferredHighlightColor.collectAsState()
    val selectedSpans by viewModel.selectedSpans.collectAsState()

    val highLightSpans = selectedSpans.filterIsInstance<IHBackgroundSpan>()
    val bookmarkSpans = selectedSpans.filterIsInstance<IHBookmarkClickableSpan>()
    val noteSpans = selectedSpans.filterIsInstance<IHNoteSpan>()

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (highLightSpans.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .background(
                        color = if (showColorsMenu.value) {
                            (BookmarkDialogBrown)
                        } else {
                            (Color.Transparent)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (showColorsMenu.value) {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        items(highlightColors) { color ->
                            if (color != preferredHighlightColor)
                                Box(modifier = Modifier
                                    .size(20.dp)
                                    .background(color, CircleShape)
                                    .clickable {
                                        viewModel.setPreferredHighlightColor(color)
                                        viewModel.textView.value?.addHighlightOnText()
                                        showColorsMenu.value = false
                                    })
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }


                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                color = preferredHighlightColor,
                                shape = CircleShape
                            )
                            .clickable {
                                showColorsMenu.value = !showColorsMenu.value
                            }
                    )


                    IconButton(
                        onClick = { // handle highlight
                            viewModel.textView.value?.addHighlightOnText()
                        }) {
                        Icon(Icons.Default.Brush, contentDescription = "Highlight")
                    }
                }

            }
        } else {
            IconButton(onClick = { // handle remove highlight
                viewModel.textView.value?.removeHighlightOnText()
            }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id =R.drawable.eraser),
                    contentDescription = "remove highlight")
            }
        }


        if (bookmarkSpans.isEmpty()){
            IconButton(onClick = { // handle Add Bookmark
                viewModel.textView.value?.addBookmarkOnText()
            }) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = "Bookmark")
            }
        } else {
//            IconButton(onClick = { // handle remove bookmark
//                viewModel.removeBookmarkById()
//            }) {
//                Icon(Icons.Default.BookmarkRemove, contentDescription = "remove bookmark")
//            }
        }

        IconButton(onClick = { // handle Add Note
            viewModel.textView.value?.addNoteOnText()
        }) {
            Icon(Icons.AutoMirrored.Filled.NoteAdd, contentDescription = "Note")
        }

        IconButton(onClick = { // handle text copy
            viewModel.textView.value?.copyText()
        }) {
            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
        }

    }

}

val highlightColors = listOf(
    HighlightFrenchLime,
    HighlightLaserLemon,
    HighlightUltraRed,
    HighlightCheeseOrange,
    HighlightMayaBlue
)