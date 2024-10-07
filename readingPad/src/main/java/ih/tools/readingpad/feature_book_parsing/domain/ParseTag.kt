package ih.tools.readingpad.feature_book_parsing.domain

import android.util.Log
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.LocalFile
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldEncoding
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement

/**
 * Parses a tag from the book content and returns a ParsedElement representing the tag typeand its content.
 *
 * @param tagContent The content of the tag, including the tag identifier and the actual content.
 * @param oldEncoding The encoding information for the book, used to identify different tag types.
 * @return A ParsedElement representing the parsed tag.
 * @throws IllegalArgumentException If the tag content is invalid or the tag type is unknown.
 */
fun parseTag(
    tagContent: String,
    oldEncoding: OldEncoding,
): ParsedElement {
    if (tagContent.length <= 1) {
        throw IllegalArgumentException("Invalid tag content: $tagContent")
    }
    val tagStart = tagContent.substring(0, 1)
    val content = tagContent.substring(1)
    val tags = oldEncoding.oldTags
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