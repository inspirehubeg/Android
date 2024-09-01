package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.text.SpannableStringBuilder
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement

/**
 * Parses regular text elements within a book and adds them to a SpannableStringBuilder.
 */
class ParseRegularText {
    /**
     * Appends the content of a regular text element to a SpannableStringBuilder.
     ** @param spannedText The SpannableStringBuilder to append the text to.
     * @param parsedTag The parsed text element containing the text content.
     * @return The SpannableStringBuilder with the text content appended.
     */
     fun invoke(
        spannedText: SpannableStringBuilder,
        parsedTag: ParsedElement.Text
    ):SpannableStringBuilder {
        spannedText.append(parsedTag.content)
        return spannedText
    }

}
