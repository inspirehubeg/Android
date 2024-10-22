package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import alexSchool.network.domain.Book
import alexSchool.network.domain.Metadata
import alexSchool.network.domain.Page
import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldBook
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldMetadata
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldPage
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.domain.parseTag
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel

/**
 * Parses the content of a page lazily, handling different elements like text, fonts, links, and images.
 * This class processes encoded page content and converts it into a SpannableStringBuilder for display.
 */
class ParsePageLazy {
    /**
     * Parses the encoded content of a book page and converts it into a SpannableStringBuilder.
     *
     * @param pageEncodedString The encoded content of the page.
     * @param oldMetadata Metadata about the book, including encoding information.
     * @param context The application context.
     * @param oldBook The book object.
     * @param lazyListState The LazyListState of the LazyColumn displaying the book content.
     * @param viewModel The ViewModel associated with the book content.
     * @return A SpannableStringBuilder containing the parsed content with formatting and interactive elements.
     */
    fun invoke(
        pageEncodedString: String,
        oldMetadata: OldMetadata,
        context: Context,
        oldBook: OldBook,
        lazyListState: LazyListState,
        viewModel: BookContentViewModel,
        uiStateViewModel: UIStateViewModel
    ): SpannableStringBuilder {
        val encoding = oldMetadata.oldEncoding
        val tagStart = encoding.oldTags.tagStart
        val tagLinkTarget = encoding.oldTags.internalLinkTarget
        var pageSpannableStringBuilder = SpannableStringBuilder()

        /**
         * Regex to match elements enclosed in tags, excluding internal link targets.
         * Matches patterns like "##...**" (start tag to end tag) but not "##l...*".
         */
        val regex = Regex(
            "$tagStart(?!$tagLinkTarget)(.*?)\\*\\*",
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
                            ParseFont().invoke(
                                oldMetadata,
                                parsedTag,
                                pageSpannableStringBuilder,
                            )
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
                            oldMetadata,
                            oldBook = oldBook,
                            lazyListState,
                            viewModel
                        )
                    }

                    is ParsedElement.Image -> {
                        pageSpannableStringBuilder =
                            ParseImage().invoke(
                                parsedTag,
                                pageSpannableStringBuilder,
                                context,
                                uiStateViewModel
                            )
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
        return pageSpannableStringBuilder
    }

    fun invoke(
        pageEncodedString: String,
        metadata: Metadata,
        context: Context,
        book: Book,
        lazyListState: LazyListState,
        viewModel: BookContentViewModel,
        uiStateViewModel: UIStateViewModel
    ): SpannableStringBuilder {
        val encoding = metadata.encoding
        val tagStart = encoding.tags.tagStart
        val tagEnd = encoding.tags.tagEnd
        val tagLinkTarget = encoding.tags.internalLinkTarget
        var pageSpannableStringBuilder = SpannableStringBuilder()

        /**
         * Regex to match elements enclosed in tags, excluding internal link targets.
         * Matches patterns like "##...@@" (start tag to end tag) but not "##l...*".
         */
        val regex = Regex(
            "$tagStart(?!$tagLinkTarget)(.*?)${Regex.escape(tagEnd)}",
            RegexOption.DOT_MATCHES_ALL
        )
        var lastIndex = 0



        regex.findAll(pageEncodedString).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1


            if (matchResult.groups[1] != null) {
                //Handle the first pattern (##(?!l)(.*?)@@)
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
                            ParseFont().invoke(
                                metadata,
                                parsedTag,
                                pageSpannableStringBuilder,
                            )
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
                            book,
                            lazyListState,
                            viewModel
                        )
                    }

                    is ParsedElement.Image -> {
                        pageSpannableStringBuilder =
                            ParseImage().invoke(
                                parsedTag,
                                pageSpannableStringBuilder,
                                context,
                                uiStateViewModel
                            )
                    }
                }
            }

            // Add normal text before the tag
            if (start > lastIndex) {
                Log.d("ParseBook", "Adding normal text: ${pageEncodedString.substring(lastIndex, start)}")
                pageSpannableStringBuilder.append(pageEncodedString.substring(lastIndex, start))
            }

            lastIndex = end
        }
        // Add the remaining text after the last tag
        if (lastIndex < pageEncodedString.length) {
            pageSpannableStringBuilder.append(pageEncodedString.substring(lastIndex))
        }
        return pageSpannableStringBuilder
    }
}

/**
 * Converts a list of pages with plain text content to a list of pages with formatted, spanned content.
 * This function applies the ParsePageLazy class to each page to parse the content and return a list of SpannedPage objects.
 *
 * @param oldPages The list of pages to convert.
 * @param oldMetadata Metadata about the book, including encoding information.
 * @param context The application context.
 * @param oldBook The book object.
 * @param lazyListState The LazyListState of the LazyColumn displaying the book content.
 * @param viewModel The ViewModel associated with the book content.
 * @return A list of SpannedPage objects, where each page has its content formatted as a SpannableStringBuilder.
 */
fun convertPagesToSpannedPagesLazy(
    oldPages: List<OldPage>,
    oldMetadata: OldMetadata,
    context: Context,
    oldBook: OldBook,
    lazyListState: LazyListState,
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel

): List<SpannedPage> {
    val parsePageLazy = ParsePageLazy() // Create a single instance of ParsePageLazy

    return oldPages.map { pageContent ->
        val spannableContent = SpannableStringBuilder(pageContent.body.text)
        // Apply formatting to spannableContent here
        val decodedSpannedPage =
            parsePageLazy.invoke(
                pageContent.body.text,
                oldMetadata,
                context,
                oldBook,
                lazyListState,
                viewModel,
                uiStateViewModel
            )
        SpannedPage(decodedSpannedPage, pageContent.pageNumber)
    }
}

fun convertPagesToSpannedPagesLazy(
    pages: List<Page>,
    metadata: Metadata,
    context: Context,
    book: Book,
    lazyListState: LazyListState,
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel
): List<SpannedPage> {
    val parsePageLazy = ParsePageLazy() // Create a single instance of ParsePageLazy
    return pages.map { pageContent ->
        val decodedSpannedPage =
            parsePageLazy.invoke(
                pageContent.body,
                metadata,
                context,
                book,
                lazyListState,
                viewModel,
                uiStateViewModel
            )
        SpannedPage(decodedSpannedPage, pageContent.pageNumber)
    }

}