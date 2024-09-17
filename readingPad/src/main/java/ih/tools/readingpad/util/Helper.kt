package ih.tools.readingpad.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.font.FontWeight
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView


val BOOKMARK_ICON = "\uD83C\uDFF7\uFE0F"
val NOTE_ICON = "\uD83D\uDCDD"
val COPY_ICON = "\uD83D\uDDD0"
val HIGHLIGHT_ICON = "\uD83D\uDD27"


/** used to copy text to the clipboard with a signature to preserve copyright status*/
fun copyTextWithSignature(text: CharSequence, context: Context) {
    val signature = " ©AlexSchool"
    val modifiedText = "$text$signature"
    Log.d("BookContentScreen", "the copy function is called with text length ${text.length}")

    // Set the modified text as the primary clip
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("Modified Text", modifiedText)
    clipboardManager.setPrimaryClip(clipData)

    // Show a toast if the selected text exceeds 150 characters
    if (text.length < 150) {
        showToast(context, "text copied to clipboard")
    } else {
        showToast(context, "cannot_copy_more_than_150_characters")
    }

}


fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

/** used to update the text view style*/
fun updateTextViewStyle(
    textView: IHTextView,
    fontSize: Float,
    fontColor: Int,
    fontWeight: Int
) {

    textView.textSize = fontSize
    textView.setTextColor(fontColor)
    textView.setTypeface(
        null,
        if (fontWeight == FontWeight.Normal.weight) Typeface.NORMAL else Typeface.BOLD
    )
}