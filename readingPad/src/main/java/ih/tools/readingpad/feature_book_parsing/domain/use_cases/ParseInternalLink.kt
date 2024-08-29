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
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Metadata
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_book_parsing.presentation.recycler_view.IHRecyclerView
import ih.tools.readingpad.feature_book_parsing.presentation.recycler_view.ViewHolder
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView

/**
 * This parses an internal link source and applies it to the spannable string
 * this one is used with the recycler view
 */
class ParseInternalLink {
     operator fun invoke(
        spannedText: SpannableStringBuilder,
        parsedTag: ParsedElement.InternalLinkSource,
        metadata: Metadata,
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
            metadata,
            parsedTag.key,
            book,
            recyclerView,
            bookContentViewModel,
        )
        return spannedText
    }
}

//applies the color and underline for a link and sets the the onClick
fun applyLinkCustomizations(
    spannable: SpannableStringBuilder,
    start: Int,
    end: Int,
    metadata: Metadata,
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
                            scrollToIndex(targetPageIndex, targetIndex!!, recyclerView)

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

