package ih.tools.readingpad.feature_book_parsing.domain.use_cases

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AlignmentSpan
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.DisplayMetrics
import android.view.View
import ih.tools.readingpad.feature_book_parsing.domain.model.ParsedElement
import ih.tools.readingpad.ui.UIStateViewModel

/**
 * Parses image elements within a book and adds them to a SpannableStringBuilder.
 */
class ParseImage {
    /**
     * Adds an image to a SpannableStringBuilder based on a parsed image element.
     *
     * @param parsedTag The parsed image element containing image data and alignment information.
     * @param spannedText The SpannableStringBuilder to add the image to.
     * @param context The application context.
     * @param viewModel The ViewModel associated with the book content.
     * @return The SpannableStringBuilder with the image added.
     */
    operator fun invoke(
        parsedTag: ParsedElement.Image,
        spannedText: SpannableStringBuilder,
        context: Context,
        uiStateViewModel: UIStateViewModel
    ): SpannableStringBuilder {
        val start = spannedText.length
        val spannedImage =
            addPhoto(parsedTag.alignment, parsedTag.content, parsedTag.ratio, context, uiStateViewModel )
        spannedText.append(spannedImage)
        return spannedText
    }
}


/**
 * Creates a SpannableString containing an image with specified alignment, size, and click functionality.
 *
 * @param align The alignment of the image ("c" for center, "n" for normal, "o" for opposite).
 * @param byteArray The byte array containing the image data.
 * @param ratio The width ratio of the image relative to the screen width.
 * @param context The application context.
 * @param viewModel The ViewModel associated with the book content.
 * @return A SpannableString containing the image with applied styles and click functionality.
 */
fun addPhoto(
    align: String,
    byteArray: ByteArray,
    ratio: Int,
    context: Context,
    uiStateViewModel: UIStateViewModel
): SpannableString {

    val spannableString = SpannableString("0") //Use a placeholder character
    val span =
        when (align) {
            "c" -> {
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER)
            }

            "n" -> {
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL)
            }

            "o" -> {
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)
            }

            else -> null
        }
    span?.let {
        spannableString.setSpan(it, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    // use ImageSpan
    val bitmapObj = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    val bitmapDrawable = BitmapDrawable(context.resources, bitmapObj)


    val width = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = (context as Activity).windowManager.currentWindowMetrics
        windowMetrics.bounds.width()
    } else {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }


    // Set the desired width and height for the image
    bitmapDrawable.setBounds(
        0,
        0,
        (width * ratio / 100),
        (width * bitmapDrawable.intrinsicHeight / bitmapDrawable.intrinsicWidth * ratio / 100)
    )
    val imageSpan = ImageSpan(bitmapDrawable, ImageSpan.ALIGN_BASELINE)

    spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)


    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            // Trigger the event to display the larger image
            //viewModel.onImageClick(byteArray)
            uiStateViewModel.onImageClick(byteArray)
        }
    }
    spannableString.setSpan(clickableSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    return spannableString
}
