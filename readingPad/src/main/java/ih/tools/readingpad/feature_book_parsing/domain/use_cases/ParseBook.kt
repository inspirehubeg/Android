package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Book
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Encoding
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.LocalFile
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Metadata
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Page
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel

class ParseBook {

    suspend fun invoke(
        pageEncodedString: String,
        metadata: Metadata,
        context: Context,
        book: Book,
        recyclerView: RecyclerView,
        viewModel: BookContentViewModel
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
                        pageSpannableStringBuilder = ParseFont().invoke(metadata, parsedTag, pageSpannableStringBuilder)

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
                        pageSpannableStringBuilder = ParseImage().invoke(parsedTag, pageSpannableStringBuilder, context, viewModel = viewModel )
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

/** this takes the parts that the regex split starting always with a tag start
 * then classify each part to the right element either an Image, internal link, web link or custom font
 */
 fun parseTag(
    tagContent: String,
    encoding: Encoding,
): ParsedElement {
    val tagStart = tagContent.substring(0, 1)
    val content = tagContent.substring(1)
    val tags = encoding.tags
    Log.d("parseTag", "Tag: $tagStart, Content: $content")

    return when (tagStart) {
        tags.webLink -> ParsedElement.WebLink(content)
        tags.internalLink -> {
            val linkKey = content.substring(0, tags.linkKeyLength.toInt())
            val linkContent = content.substring(tags.linkKeyLength.toInt())
            //saveLinkSource(viewModel, start, end, linkKey)
            ParsedElement.InternalLinkSource(linkContent, linkKey)
        }

        tags.image -> {
            lateinit var imageData: String
            lateinit var imageRatio: String
            lateinit var imageAlign: String

            val sub = content.split(':')
            imageData = sub[0] //image data byte array
            imageRatio = sub[1] //image ratio
            imageAlign = sub[2] //image alignment

            val localFile = LocalFile.instance()
            val decodedImage1 = localFile.decode(imageData)
            Log.d("imageParser", "Image: $imageRatio")
            Log.d("imageParser", "Image: $imageAlign")


            ParsedElement.Image(
                content = decodedImage1,
                alignment = imageAlign,
                ratio = imageRatio.toInt()
            )
        }


        else -> ParsedElement.Font(content, tagStart)
    }
}


suspend fun convertPagesToSpannedPages(
    pages: List<Page>,
    metadata: Metadata,
    context: Context,
    book: Book,
    recyclerView: RecyclerView,
    bookContentViewModel: BookContentViewModel
): List<SpannedPage> {
    return pages.map { pageContent ->
        // Apply formatting to spannableContent here
        val decodedSpannedPage =
            ParseBook().invoke(pageContent.body.text, metadata, context, book, recyclerView,bookContentViewModel)
        SpannedPage(decodedSpannedPage, pageContent.pageNumber)
    }
}
