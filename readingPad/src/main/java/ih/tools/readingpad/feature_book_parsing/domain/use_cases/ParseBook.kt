package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.content.Context
import android.os.Build
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Book
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Encoding
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.LocalFile
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Metadata
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Page
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel

/**
 * Parses the content of a book, handling different elements like text, fonts, links, and images.
 */
class ParseBook {

/**
 * Parses the encoded content of a book page and converts it into a SpannableStringBuilder.
 *
 * @param pageEncodedString The encoded content of the page.
 * @param metadata Metadata about the book, including encoding information.
 * @param context The application context.
 * @param book The book object.
 * @param recyclerView The RecyclerView used to display the book content.
 * @param viewModel The ViewModel associated with the book content.
 * @return A SpannableStringBuilder containing the parsed content with formatting and interactive elements.
 */
    @RequiresApi(Build.VERSION_CODES.P)
     fun invoke(
        pageEncodedString: String,
        metadata: Metadata,
        context: Context,
        book: Book,
        recyclerView: RecyclerView,
        viewModel: BookContentViewModel,
        uiStateViewModel: UIStateViewModel
    ): SpannableStringBuilder {
        val encoding = metadata.encoding
        val TAG_START = encoding.tags.tagStart
        val TAG_LINK_TARGET = encoding.tags.internalLinkTarget
        var pageSpannableStringBuilder = SpannableStringBuilder()

        /**
         * Regex takes a pattern and returns all the results which lies between the pattern
         * here for example the pattern is either start tag to end tag
         * or start tag and link target tag (it has no end tag)
         */
        val regex = Regex(
            "$TAG_START(?!$TAG_LINK_TARGET)(.*?)\\*\\*",
            RegexOption.DOT_MATCHES_ALL // This option allows . to match newline characters
        )
        var lastIndex = 0



        regex.findAll(pageEncodedString).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1


            if (matchResult.groups[1] != null) {
                //Handle the first pattern (##(?!l)(.*?)**)
                val tagContent = matchResult.groupValues[1]
                when (val parsedTag = parseTag(tagContent, encoding)) {
                    // if the parsed tag has no custom fonts or images it will be regular text
                    is ParsedElement.Text -> {
                        Log.d("ParseBook", "Text element is ${parsedTag.content}")
                        pageSpannableStringBuilder = ParseRegularText().invoke(pageSpannableStringBuilder, parsedTag)

                    }
                    // if the parsed tag is a custom font
                    is ParsedElement.Font -> {
                        Log.d("ParseBook", "Font element is ${parsedTag.content}")
                        pageSpannableStringBuilder = ParseFont().invoke(metadata, parsedTag, pageSpannableStringBuilder,context)

                    }

                    // if the parsed tag is a web link
                    is ParsedElement.WebLink -> {
                        Log.d("ParseBook", "Weblink element is ${parsedTag.content}")
                        pageSpannableStringBuilder = ParseWebLink().invoke(pageSpannableStringBuilder, parsedTag)
                    }

                    // if the parsed tag is an internal link
                    is ParsedElement.InternalLinkSource -> {
                        pageSpannableStringBuilder = ParseInternalLink().invoke(pageSpannableStringBuilder, parsedTag, metadata, book = book , recyclerView, viewModel)
                    }

                    // if the parsed tag is an image
                    is ParsedElement.Image -> {
                        Log.d("ParseBook", "Image element is ${parsedTag.content}")
                        pageSpannableStringBuilder = ParseImage().invoke(parsedTag, pageSpannableStringBuilder, context, uiStateViewModel = uiStateViewModel )
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
}

/**
 * Parses a tag from the book content and returns a ParsedElement representing the tag typeand its content.
 *
 * @param tagContent The content of the tag, including the tag identifier and the actual content.
 * @param encoding The encoding information for the book, used to identify different tag types.
 * @return A ParsedElement representing the parsed tag.
 * @throws IllegalArgumentException If the tag content is invalid or the tag type is unknown.
 */
 fun parseTag(
    tagContent: String,
    encoding: Encoding,
): ParsedElement {
    if (tagContent.length <= 1) {
        throw IllegalArgumentException("Invalid tag content: $tagContent")
    }
    val tagStart = tagContent.substring(0, 1)
    val content = tagContent.substring(1)
    val tags = encoding.tags
    Log.d("parseTag", "Tag: $tagStart, Content: $content")

    return when (tagStart) {
        tags.webLink -> ParsedElement.WebLink(content)
        tags.internalLink -> {
            if (content.length <= tags.linkKeyLength) {
                throw IllegalArgumentException("Invalid internal link content: $content")
            }
            val linkKey = content.substring(0, tags.linkKeyLength)
            val linkContent = content.substring(tags.linkKeyLength)
            ParsedElement.InternalLinkSource(linkContent, linkKey)
        }

        tags.image -> {
            val sub = content.split(':')
            if (sub.size != 3) {
                throw IllegalArgumentException("Invalid image content: $content")
            }
           val imageData = sub[0] //image data byte array
            val imageRatio = sub[1] //image ratio
            val imageAlign = sub[2] //image alignment

            val localFile = LocalFile.instance()
            val decodedImage1 = localFile.decode(imageData)
            Log.d("imageParser", "Image: $imageRatio")
            Log.d("imageParser", "Image: $imageAlign")


            ParsedElement.Image(
                content = decodedImage1,
                alignment = imageAlign,
                ratio = imageRatio.toIntOrNull() ?: 0 // Handle potential NumberFormatException
            )
        }
        else -> ParsedElement.Font(content, tagStart)
    }
}

/**
 * Converts a list of pages with plain text content to a list of pages with formatted, spanned content.
 *
 * @param pages The list of pages to convert.
 * @param metadata Metadata about the book, including encoding information.
 * @param context The application context.
 * @param book The book object.
 * @param recyclerView The RecyclerView used to display the book content.
 * @param bookContentViewModel The ViewModel associated with the book content.
 * @return A list of SpannedPage objects, where each page has its content formatted as a SpannableStringBuilder.
 */
suspend fun convertPagesToSpannedPages(
    pages: List<Page>,
    metadata: Metadata,
    context: Context,
    book: Book,
    recyclerView: RecyclerView,
    bookContentViewModel: BookContentViewModel
): List<SpannedPage> {
    val parseBook = ParseBook() // Create a single instance of ParseBook
    return pages.map { pageContent ->
        // Apply formatting to spannableContent here
//        val decodedSpannedPage =
//            parseBook.invoke(pageContent.body.text, metadata, context, book, recyclerView,bookContentViewModel)
        val spannableStringBuilder = SpannableStringBuilder()
        SpannedPage(spannableStringBuilder, pageContent.pageNumber)
    }
}
