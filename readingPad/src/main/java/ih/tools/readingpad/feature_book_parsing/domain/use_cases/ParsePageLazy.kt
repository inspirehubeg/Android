package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Book
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Metadata
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Page
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel

/** this takes a page content as encoded string to decode it and apply different spanning to it
 * then return it as a spannable string to be displayed in the text view
 */
class ParsePageLazy {
    suspend fun invoke(
        pageEncodedString: String,
        metadata: Metadata,
        context: Context,
        book: Book,
        lazyListState: LazyListState,
        viewModel: BookContentViewModel
    ): SpannableStringBuilder {
        val encoding = metadata.encoding
        val TAG_START = encoding.tags.tagStart
        val TAG_LINK_TARGET = encoding.tags.internalLinkTarget
        var pageSpannableStringBuilder = SpannableStringBuilder()

        // Regex splits the string according to different tags to get different parsed elements
        val regex = Regex(
            "$TAG_START(?!$TAG_LINK_TARGET)(.*?)\\*\\*",
            RegexOption.DOT_MATCHES_ALL
        )
        var lastIndex = 0



        regex.findAll(pageEncodedString).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1


            if (matchResult.groups[1] != null) {
                //Handle the first pattern (##(?!l)(.*?)**)
                val tagContent = matchResult.groupValues[1]
                when (val parsedTag = parseTag(tagContent, encoding)) {
                    is ParsedElement.Text -> {
                        Log.d("ParseBook", "Text element is ${parsedTag.content}")
                        pageSpannableStringBuilder =
                            ParseRegularText().invoke(pageSpannableStringBuilder, parsedTag)
                    }

                    is ParsedElement.Font -> {
                        Log.d("ParseBook", "Font element is ${parsedTag.content}")
                        pageSpannableStringBuilder =
                            ParseFont().invoke(metadata, parsedTag, pageSpannableStringBuilder)
                    }

                    is ParsedElement.WebLink -> {
                        Log.d("ParseBook", "Weblink element is ${parsedTag.content}")
                        pageSpannableStringBuilder =
                            ParseWebLink().invoke(pageSpannableStringBuilder, parsedTag)
                    }

                    is ParsedElement.InternalLinkSource -> {
                        pageSpannableStringBuilder = ParseInternalLinkLazy().invoke(
                            pageSpannableStringBuilder,
                            parsedTag,
                            metadata,
                            book = book,
                            lazyListState,
                            viewModel
                        )
                    }

                    is ParsedElement.Image -> {
                        Log.d("ParseBook", "Image element is ${parsedTag.content}")
                        pageSpannableStringBuilder =
                            ParseImage().invoke(parsedTag, pageSpannableStringBuilder, context, viewModel = viewModel)
                    }

                }

            }

            // Add normal text before the tag
            if (start > lastIndex) {
                pageSpannableStringBuilder.append(pageEncodedString.substring(lastIndex, start))
            }

            lastIndex = end
        }
        // Add the remaining text after the last tag
        if (lastIndex < pageEncodedString.length) {
            pageSpannableStringBuilder.append(pageEncodedString.substring(lastIndex))
        }
        // textView.text = spannedBook
        return pageSpannableStringBuilder
    }
}

/** This applies the Parse page lazy class to each page in the book to parse the page content
 * and return a list of spanned pages with their page numbers
 */
suspend fun convertPagesToSpannedPagesLazy(
    pages: List<Page>,
    metadata: Metadata,
    context: Context,
    book: Book,
    lazyListState: LazyListState,
    bookContentViewModel: BookContentViewModel
): List<SpannedPage> {
    return pages.map { pageContent ->
        val spannableContent = SpannableStringBuilder(pageContent.body.text)
        // Apply formatting to spannableContent here
        val decodedSpannedPage =
            ParsePageLazy().invoke(
                pageContent.body.text,
                metadata,
                context,
                book,
                lazyListState,
                bookContentViewModel
            )
        SpannedPage(decodedSpannedPage, pageContent.pageNumber)
    }
}