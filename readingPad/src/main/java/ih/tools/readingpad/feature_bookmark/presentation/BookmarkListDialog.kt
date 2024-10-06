package ih.tools.readingpad.feature_bookmark.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.ui.UIStateViewModel


/** A custom dialog to show the book bookmarks and allow the user to navigate to any of it*/
@Composable
fun BookmarkListDialog(
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel,
    dialogWidth: Dp,
    listState: LazyListState,
) {
    val bookmarks = viewModel.state.value.bookBookmarkEntities
   // val fontSize by viewModel.fontSize.collectAsState()
    val fontSize = uiStateViewModel.uiSettings.collectAsState().value.fontSize
    val onDismiss = {

    }
    //an outer box that fills the entire screen but transparent to allow the custom placement of the inner box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = {
                onDismiss()
            })
        , contentAlignment = Alignment.Center
    ) {
        // the actual dialog box placed in the desired location of the screen, in this case at the center
        Box(
            modifier = Modifier
                //.height(dialogHeight)
                .width(dialogWidth)
                .padding(vertical = 50.dp)
                .background(
                    MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
                .align(Alignment.Center)
                .clickable(onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                )
                {
                    Text(
                        text = stringResource(R.string.book_bookmarks),
                        fontSize = 26.sp,
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        textDecoration = TextDecoration.Underline
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (bookmarks.isEmpty()) {
                    Text(text = "No bookmarks Saved yet", fontSize = fontSize.sp)
                } else {
                    //LazyColumn to allow the list to grow dynamically and to allow scrolling
                    LazyColumn {
                        items(bookmarks) { bookmark ->
                            BookmarkListItem(
                                bookmark = bookmark,
                                viewModel = viewModel,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.scrollToIndexLazy(
                                            targetPageIndex = bookmark.pageNumber - 1,
                                            lazyListState = listState,
                                            targetIndex = bookmark.start
                                        )
                                        onDismiss()
                                    }
                            )
                        }
                    }
                }
            }

        }
    }
}

//
//@Preview
//@Composable
//fun PreviewBookmarkListDialog() {
//    // BookmarkListDialog()
//}

/** Single Bookmark item inside the list*/
@Composable
fun BookmarkListItem(
    bookmark: Bookmark,
    modifier: Modifier,
    viewModel: BookContentViewModel
) {
    val words = bookmark.bookmarkTitle.split(" ")
    //only uses the first 3 words of the bookmark name
    val croppedTitle = if (words.size > 7) {
        words.subList(0, 7).joinToString(" ") + "..."
    } else {
        bookmark.bookmarkTitle
    }
    //make sure that the first 3 words length doesn't exceed a certain limit to maintain the menu looks
    val shortenedTitle = if (croppedTitle.length > 20) {
        croppedTitle.substring(0, 20) + "..."
    } else {
        croppedTitle
    }
    Card(
        modifier = Modifier.padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
    {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(5f)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "* $shortenedTitle", fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        // modifier = Modifier.weight(5f)
                    )

                }
                Spacer(
                    modifier = Modifier
                        .height(3.dp)
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(MaterialTheme.colorScheme.outline)
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.chapter_number, bookmark.chapterNumber),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(R.string.page_number, bookmark.pageNumber),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            FilledIconButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onClick = {
                    viewModel.removeBookmarkById(bookmark.id!!)
                },
                colors = androidx.compose.material3.IconButtonDefaults.filledIconButtonColors(
                    //containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    //contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Bookmark")
            }
        }

    }


    Spacer(modifier = Modifier.height(16.dp))
}