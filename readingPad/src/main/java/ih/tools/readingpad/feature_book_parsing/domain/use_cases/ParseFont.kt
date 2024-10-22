package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import alexSchool.network.domain.BookFont
import alexSchool.network.domain.Metadata
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
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Font
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.OldMetadata
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement

/**
 * Parses font style elements within a book and applies them to a SpannableStringBuilder.
 */
class ParseFont {
    /**
     * Applies font customizations to a SpannableStringBuilder based on a parsed font element.
     *
     * @param oldMetadata Metadata about the book, including encoding and font information.
     * @param parsedTag The parsed font element containing style information.
     * @param spannedText The SpannableStringBuilder to apply font styles to.
     * @return The SpannableStringBuilder with applied font styles.
     */
    operator fun invoke(
        oldMetadata: OldMetadata,
        parsedTag: ParsedElement.Font,
        spannedText: SpannableStringBuilder,
    ): SpannableStringBuilder {

        val start = spannedText.length
        spannedText.append(parsedTag.content)
        val end = spannedText.length

        applyFontCustomizations(
            spannedText,
            parsedTag.fontTag,
            start,
            end,
            fonts = oldMetadata.oldEncoding.fonts,
        )
        return spannedText
    }

    operator fun invoke(
        metadata: Metadata,
        parsedTag: ParsedElement.Font,
        spannedText: SpannableStringBuilder,
    ): SpannableStringBuilder {

        val start = spannedText.length
        spannedText.append(parsedTag.content)
        val end = spannedText.length

        applyFontCustomizations(
            spannable = spannedText,
            fontTag = parsedTag.fontTag,
            start = start,
            end = end,
            fonts = metadata.encoding.fonts,
        )
        return spannedText
    }
}

/**
 * context.filesDir provides:
 * Location: It points to a directory within the device's internal storage that is specifically allocated for your app.
 * Path: The path is usually something like /data/data/[your_app_package_name]/files.
 * Access: Only your app has read and write access to this directory.
 * Persistence: Files stored here are persistent and remain even after the app is closed or the device is restarted.
 * Deletion: Files in this directory are deleted when the app is uninstalled
 */
//fun getTypefaceSpan(context: Context, fontFileName: String): TypefaceSpan {
//    val fontFile = File(context.filesDir, fontFileName) // Check internal storage
//    return if (fontFile.exists()) {
//        val typeface = Typeface.createFromFile(fontFile)
//        TypefaceSpan(typeface)
//    } else {
//        // Use default TypefaceSpan
//        TypefaceSpan(Typeface.DEFAULT)
//    }
//}

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
    fonts: Map<String, Font>,
) {

    val fontStyle = fonts[fontTag] ?: return // Return early if font style is not found
    /**
     * managing different font families using this way requires android v 28 and above
     */
//    val bookerlyNormalTypeface = ResourcesCompat.getFont(context, R.font.bookerly_regular)?.let {
//        TypefaceSpan(it)
//    }
//
//    val customFontTypeface : TypefaceSpan = getTypefaceSpan(context, "bookerly_bold_italic.ttf")

    if (fontStyle.bold == "1") {
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    if (fontStyle.italic == "1") {
        spannable.setSpan(StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    if (fontStyle.underline == "1") {
        //spannable.setSpan(bookerlyNormalTypeface, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // spannable.setSpan(customFontTypeface, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
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
        spannable.setSpan(BackgroundColorSpan(Color.parseColor(fontStyle.backgroundColor)),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )


    Log.d("ApplyFont", "Applied font style: $fontStyle from $start to $end")
}

fun applyFontCustomizations(
    spannable: SpannableStringBuilder,
    fontTag: String,
    start: Int,
    fonts: Map<String, BookFont>,
    end: Int,
) {

    val fontStyle = fonts[fontTag] ?: return // Return early if font style is not found
    /**
     * managing different font families using this way requires android v 28 and above
     */
//    val bookerlyNormalTypeface = ResourcesCompat.getFont(context, R.font.bookerly_regular)?.let {
//        TypefaceSpan(it)
//    }
//
//    val customFontTypeface : TypefaceSpan = getTypefaceSpan(context, "bookerly_bold_italic.ttf")

    if (fontStyle.bold == "1") {
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    if (fontStyle.italic == "1") {
        spannable.setSpan(StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    if (fontStyle.underline == "1") {
        //spannable.setSpan(bookerlyNormalTypeface, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // spannable.setSpan(customFontTypeface, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
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
        ForegroundColorSpan(fontStyle.fontColor.toInt()),
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    if (fontStyle.backgroundColor != "0")
        spannable.setSpan(
            BackgroundColorSpan(fontStyle.backgroundColor.toInt()),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )


    Log.d("ApplyFont", "Applied font style: $fontStyle from $start to $end")
}
