package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.graphics.Color
import android.graphics.Typeface
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AlignmentSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import book_reader.Font
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Metadata
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement

/**
 * Parses font style elements within a book and applies them to a SpannableStringBuilder.
 */
class ParseFont {
    /**
     * Applies font customizations to a SpannableStringBuilder based on a parsed font element.
     *
     * @param metadata Metadata about the book, including encoding and font information.
     * @param parsedTag The parsed font element containing style information.
     * @param spannedText The SpannableStringBuilder to apply font styles to.
     * @return The SpannableStringBuilder with applied font styles.
     */
    operator fun invoke(
        metadata: Metadata,
        parsedTag: ParsedElement.Font,
        spannedText: SpannableStringBuilder
    ): SpannableStringBuilder {

        val start = spannedText.length
        spannedText.append(parsedTag.content)
        val end = spannedText.length

        applyFontCustomizations(
            spannedText,
            parsedTag.fontTag,
            start,
            end,
            metadata.encoding.fonts
        )
        return spannedText
    }
}

/**
 * Applies font customizations to a SpannableStringBuilder based on a font tag and style information.
 *
 * @param spannable The SpannableStringBuilder to apply styles to.
 * @param fontTag The tag representing the font style.
 * @param start The starting index of the text to style.
 * @param end The ending index of the text to style.
 * @param fonts A map of font tags to their corresponding style information.
 */

fun applyFontCustomizations(
    spannable: SpannableStringBuilder,
    fontTag: String,
    start: Int,
    end: Int,
    fonts: Map<String, Font>
) {
    val fontStyle = fonts[fontTag] ?: return // Return early if font style is not found

    if (fontStyle.bold == "1") {
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    if (fontStyle.italic == "1") {
        spannable.setSpan(StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    if (fontStyle.underline == "1") {
        spannable.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    //center alignment
    when (fontStyle.align) {
        "c" -> {
            spannable.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        // end alignment
        "1" -> {
            spannable.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        // start alignment
        "0" -> {
            spannable.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    fontStyle.size?.let {
        spannable.setSpan(
            RelativeSizeSpan(it.toFloat()),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    spannable.setSpan(
        ForegroundColorSpan(Color.parseColor(fontStyle.fontColor)),
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    if (fontStyle.backgroundColor != "0")
        spannable.setSpan(
            BackgroundColorSpan(Color.parseColor(fontStyle.backgroundColor)),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )


    Log.d("ApplyFont", "Applied font style: $fontStyle from $start to $end")
}
