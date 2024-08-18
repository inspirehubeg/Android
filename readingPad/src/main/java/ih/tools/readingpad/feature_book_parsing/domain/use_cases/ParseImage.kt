package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AlignmentSpan
import android.text.style.ImageSpan
import android.util.DisplayMetrics
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement

class ParseImage {
    suspend operator fun invoke(
        parsedTag: ParsedElement.Image,
        spannedText: SpannableStringBuilder,
        context: Context
    ): SpannableStringBuilder {
        val start = spannedText.length
        val spannedImage =
            addPhoto(parsedTag.alignment, parsedTag.content, parsedTag.ratio, context)
        spannedText.append(spannedImage)
        return spannedText
    }
}

/**
 * produce a spannable string with the image
 */
fun addPhoto(align: String, byteArray: ByteArray, ratio: Int, context: Context): SpannableString {
    val spannableString = SpannableString("0")
    var span: Any? = null
    if (align == "c") {
        span = AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER)
    } else if (align == "n") {
        span = AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL)
    } else if (align == "o") {
        span = AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)
    }

    spannableString.setSpan(span, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    // use ImageSpan
    // val image = BitmapDrawable(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))
    val bitmapObj = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    val bitmapDrawable = BitmapDrawable(context.resources, bitmapObj)


    val displayMetrics = DisplayMetrics()
    (context as Activity).windowManager
        .defaultDisplay
        .getMetrics(displayMetrics)
    val width = displayMetrics.widthPixels

    // Set the desired width and height for the image
    bitmapDrawable.setBounds(
        0,
        0,
        (width * ratio / 100),
        (width * bitmapDrawable.intrinsicHeight / bitmapDrawable.intrinsicWidth * ratio / 100)
    )
    span = ImageSpan(bitmapDrawable, ImageSpan.ALIGN_BASELINE)

    spannableString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    return spannableString
}
