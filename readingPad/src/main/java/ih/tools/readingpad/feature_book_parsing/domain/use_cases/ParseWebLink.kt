package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement

class ParseWebLink {


    suspend fun invoke(
        spannedText: SpannableStringBuilder,
        parsedTag: ParsedElement.WebLink,
        ) : SpannableStringBuilder {

        // use URLspan
        val start = spannedText.length
        spannedText.setSpan(
            URLSpan(parsedTag.content),
            0,
            parsedTag.content.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannedText
    }
}


