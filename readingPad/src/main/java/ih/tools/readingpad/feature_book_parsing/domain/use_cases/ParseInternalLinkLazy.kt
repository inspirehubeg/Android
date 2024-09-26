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
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldMetadata
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView
import ih.tools.readingpad.ui.theme.LightBlue

/**
 * Parses internal link source elements within a book and applies them to a SpannableStringBuilder.
 * This class is specifically designed for use with Lazy Column to handle internal navigation within the book.
 */
class ParseInternalLinkLazy {
    /**
     * Applies internal link customizations to a SpannableStringBuilder based on a parsed link element.
     *
     * @param spannedText The SpannableStringBuilder to apply the link to.
     * @param parsedTag The parsed internal link element containing link information.
     * @param oldMetadata Metadata about the book, including target link information.
     * @param book The book object.
     * @param lazyListState The LazyListState of the LazyColumn displaying the book content.
     * @param bookContentViewModel The ViewModel associated with the book content.
     * @return The SpannableStringBuilder with the internal link applied.
     */
     operator fun invoke(
        spannedText: SpannableStringBuilder,
        parsedTag: ParsedElement.InternalLinkSource,
        oldMetadata: OldMetadata,
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
            oldMetadata,
            parsedTag.key,
            book,
            lazyListState,
            bookContentViewModel,
        )

        return spannedText
    }
}

/**
 * Applies customizations to an internal link within a SpannableStringBuilder.
 * This function is designed for use with LazyColumn and handles navigation to the target page.
 *
 * @param spannable The SpannableStringBuilder containing the link.
 * @param start The starting index of the link text.
 * @param end The ending index of the link text.
 * @param oldMetadata Metadata about the book, including target link information.
 * @param key The key identifying the target link.
 * @param book The book object.
 * @param lazyListState The LazyListState of the LazyColumn displaying the book content.
 * @param bookContentViewModel The ViewModel associated with the book content.
 */
fun applyLinkCustomizationsLazy(
    spannable: SpannableStringBuilder,
    start: Int,
    end: Int,
    oldMetadata: OldMetadata,
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
            Log.d("onTouch", "onClick called")
            //val textView = widget as IHTextView
            if (widget is IHTextView) {
                if (widget.pressStart != -1 && (widget.pressStart <= start || widget.pressStart >= end)){
                    Log.d("onTouch", "clicked out of scope")
                    return
                }
            }
            val targetLink = oldMetadata.oldTargetLinks.find { it.key == key } ?: return


            val targetChapterNumber = targetLink.chapterNumber
            val targetPageNumber = targetLink.pageNumber
            val targetIndex = targetLink.index

            book.chapters.forEachIndexed { chapterIndex, chapter ->
                if (targetChapterNumber == chapterIndex + 1) {
                    chapter.pages.forEachIndexed { pageIndex, _ ->
                        if (targetPageNumber == pageIndex + 1) {
                            if (widget is IHTextView) {
                                bookContentViewModel.scrollToIndexLazy(pageIndex, lazyListState, targetIndex)
                            }
                            return // Exit the loops once the target page is found
                        }
                    }
                }
            }
        }
    }
    spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}