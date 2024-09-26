package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Book
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldMetadata
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_book_parsing.presentation.recycler_view.IHRecyclerView
import ih.tools.readingpad.feature_book_parsing.presentation.recycler_view.ViewHolder
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView

/**
 * Parses internal link source elements within a book and applies them to a SpannableStringBuilder.
 * This class is specifically designed for use with RecyclerViewto handle internal navigation within the book.
 */
class ParseInternalLink {
    /**
     * Applies internal link customizations to a SpannableStringBuilder based on a parsed link element.
     *
     * @param spannedText The SpannableStringBuilder to apply the link to.
     * @param parsedTag The parsed internal link element containing link information.
     * @param oldMetadata Metadata about the book, including target link information.
     * @param book The book object.
     * @param recyclerView The RecyclerView displaying the book content.
     * @param bookContentViewModel The ViewModel associated with the book content.
     * @return The SpannableStringBuilder with the internal link applied.
     */
    operator fun invoke(
        spannedText: SpannableStringBuilder,
        parsedTag: ParsedElement.InternalLinkSource,
        oldMetadata: OldMetadata,
        book: Book,
        recyclerView: RecyclerView,
        bookContentViewModel: BookContentViewModel,
    ): SpannableStringBuilder {
        val start = spannedText.length
        spannedText.append(parsedTag.content)
        val end = spannedText.length
        applyLinkCustomizations(
            spannable = spannedText,
            start,
            end,
            oldMetadata,
            parsedTag.key,
            book,
            recyclerView,
            bookContentViewModel,
        )
        return spannedText
    }
}

//applies the color and underline for a link and sets the the onClick

/**
 * Applies customizations to an internal link within a SpannableStringBuilder.
 * This function is designed for use with RecyclerView and handles navigation to the target page and index.
 *
 * @param spannable The SpannableStringBuilder containing the link.
 * @param start The starting index of the link text.
 * @param end The ending index of the link text.
 * @param oldMetadata Metadata about the book, including target link information.
 * @param key The key identifying the target link.
 * @param book The book object.
 * @param recyclerView The RecyclerView displaying the book content.
 * @param bookContentViewModel The ViewModel associated with the book content.
 */
fun applyLinkCustomizations(
    spannable: SpannableStringBuilder,
    start: Int,
    end: Int,
    oldMetadata: OldMetadata,
    key: String,
    book: Book,
    recyclerView: RecyclerView,
    bookContentViewModel: BookContentViewModel,
) {
    val clickableSpan = object : ClickableSpan() {

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = Color.BLUE
            ds.isUnderlineText = true
        }

        override fun onClick(p0: View) {
            val targetLink = oldMetadata.oldTargetLinks.find { it.key == key } ?: return
            val targetPageNumber = targetLink.pageNumber
            val targetIndex = targetLink.index
            val targetChapterNumber = targetLink.chapterNumber
            val targetBookId = targetLink?.bookId
            val bookChapters = book.chapters
            book.chapters.forEachIndexed { chapterIndex, chapter ->
                if (targetChapterNumber == chapterIndex + 1) {
                    chapter.pages.forEachIndexed { pageIndex, _ ->
                        if (targetPageNumber == pageIndex + 1) {
                            scrollToIndex(pageIndex, targetIndex, recyclerView)
                            return // Exit the loops once the target page is found
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


// scrolls to the target index in the target page
/**
 * Scrolls the RecyclerView to the target page and index within the page.
 *
 * @param targetPageIndex The index of the target page in the RecyclerView.
 * @param targetIndex The index of the target line within the target page.
 * @param recyclerView The RecyclerView displaying the book content.
 */
fun scrollToIndex(targetPageIndex: Int, targetIndex: Int, recyclerView: RecyclerView) {

    //check if we are already in the target page so no item navigation required
    val isTargetPageVisible = recyclerView.findViewHolderForAdapterPosition(targetPageIndex) != null
    if (isTargetPageVisible) {
        Log.d("parseInternalLink", "targetPage is visible")
        // Target page is already visible, scroll directly to the index
        val pageViewHolder =
            recyclerView.findViewHolderForAdapterPosition(targetPageIndex) as? ViewHolder
        pageViewHolder?.let {
            (pageViewHolder.itemView as IHTextView).scrollToIndexRecycler(recyclerView, targetIndex)
        }
    } else {
        // Target page is not visible, use the scroll listener as before

        (recyclerView as IHRecyclerView).scrollToPage(targetPageIndex)

        Log.d("parseInternalLink", "targetPage reached")
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (targetIndex >= 0) {
                        val pageViewHolder =
                            recyclerView.findViewHolderForAdapterPosition(targetPageIndex) as? ViewHolder
                        val textView = (pageViewHolder?.itemView as IHTextView)
                        pageViewHolder.let {
                            textView.scrollToIndexRecycler(recyclerView, targetIndex)
                            Log.d("parseInternalLink", "navigated to target line")
                        }
                    }
                    // Remove the listener after navigation is complete
                    recyclerView.removeOnScrollListener(this)
                }
            }
        }
        )
    }
}

