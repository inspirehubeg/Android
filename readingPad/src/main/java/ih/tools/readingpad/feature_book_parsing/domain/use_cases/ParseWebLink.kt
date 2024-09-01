package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.URLSpan
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement

/**
 * Parses web link elements within a book and applies them as clickable links to a SpannableStringBuilder.
 */
class ParseWebLink {

    /*** Applies a web link to a SpannableStringBuilder, making it clickable.
     *
     * @param spannedText The SpannableStringBuilder to apply the link to.
     * @param parsedTag The parsed web link element containing the link URL.
     * @return The SpannableStringBuilder with the web link applied.
     */
    operator fun invoke(
        spannedText: SpannableStringBuilder,
        parsedTag: ParsedElement.WebLink
    ): SpannableStringBuilder {
        val start = spannedText.length
        spannedText.append(parsedTag.content)
        val end = spannedText.length
        spannedText.setSpan(
            URLSpan(parsedTag.content),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannedText
    }
}


