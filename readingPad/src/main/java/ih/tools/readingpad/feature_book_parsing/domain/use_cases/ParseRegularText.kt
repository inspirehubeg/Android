package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.text.SpannableStringBuilder
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement

class ParseRegularText {

    suspend fun invoke(
        spannedText: SpannableStringBuilder,
        parsedTag: ParsedElement.Text
    ):SpannableStringBuilder {
        spannedText.append(parsedTag.content)
        return spannedText
    }

}
