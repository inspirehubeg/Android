package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.graphics.toArgb
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Book
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Metadata
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView
import ih.tools.readingpad.ui.theme.LightBlue

/**
 * This parses an internal link source and applies it to the spannable string
 * this one is used with the lazy column
 */
class ParseInternalLinkLazy {
    suspend operator fun invoke(
        spannedText: SpannableStringBuilder,
        parsedTag: ParsedElement.InternalLinkSource,
        metadata: Metadata,
        book: Book,
        lazyListState: LazyListState,
        bookContentViewModel: BookContentViewModel,
    ): SpannableStringBuilder {
        val start = spannedText.length
        spannedText.append(parsedTag.content)
        val end = spannedText.length
        applyLinkCustomizationsLazy(
            spannable = spannedText,
            start,
            end,
            metadata,
            parsedTag.key,
            book,
            lazyListState,
            bookContentViewModel,
        )

        return spannedText
    }

}

//used with the lazy column
fun applyLinkCustomizationsLazy(
    spannable: SpannableStringBuilder,
    start: Int,
    end: Int,
    metadata: Metadata,
    key: String,
    book: Book,
    lazyListState: LazyListState,
    bookContentViewModel: BookContentViewModel,
) {
    // val isDarkTheme = bookContentViewModel.darkTheme.collectAsState().value
    val clickableSpan = object : ClickableSpan() {

        override fun updateDrawState(ds: TextPaint) {
            Log.d("onClick", "link created")
            super.updateDrawState(ds)

            ds.color = LightBlue.toArgb()
            ds.isUnderlineText = true
        }

        override fun onClick(widget: View) {
            val targetLinks = metadata.targetLinks
            val targetLink = targetLinks.find { it.key == key }
            val targetPageNumber = targetLink?.pageNumber
            val targetIndex = targetLink?.index
            val targetChapterNumber = targetLink?.chapterNumber
            val targetBookId = targetLink?.bookId
            val bookChapters = book.chapters
            Log.d("onClick", "targetPageNumber = $targetPageNumber")
            for (i in bookChapters.indices) {
                if (targetChapterNumber == i + 1) {
                    val chapter = bookChapters[i]
                    val chapterPages = chapter.pages
                    for (j in chapterPages.indices) {
                        if (targetPageNumber == j + 1) {
                            val targetPageIndex = j
                            if (widget is IHTextView){
                                bookContentViewModel.scrollToIndexLazy(targetPageIndex,lazyListState, targetIndex!! )
                            }
                        } else {
                            // fetch the target chapter from db
                        }
                    }
                }
            }
        }
    }
    spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}