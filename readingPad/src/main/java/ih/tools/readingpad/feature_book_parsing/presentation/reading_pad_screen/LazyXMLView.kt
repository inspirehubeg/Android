package ih.tools.readingpad.feature_book_parsing.presentation.reading_pad_screen

import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView
import ih.tools.readingpad.util.updateTextViewStyle

/**this is used as the item of the lazy column each XML view represents a textView represents a single page*/
@Composable
fun XMLViewLazyItem(
    page: SpannedPage,
    viewModel: BookContentViewModel,
    modifier: Modifier
) {
    // Create a SpannableString from the book content
    val spannableString = remember {
        mutableStateOf(page.content)
    }
    val fontSize by viewModel.fontSize.collectAsState()
    val fontColor by viewModel.fontColor.collectAsState()
    val fontWeight by viewModel.fontWeight.collectAsState()

    AndroidView(
        modifier = modifier,
        factory = { currentContext ->
            // Inflate the XML layout
            val view = LayoutInflater.from(currentContext)
                .inflate(R.layout.ih_text_view_layout, null, false)

            val textView: IHTextView = view.findViewById(R.id.ihTextView)

            textView.movementMethod = LinkMovementMethod.getInstance() //enables the navigation
            // at the creation of each TextView we set the text with the page content
            textView.setText(spannableString.value, viewModel, page.pageNumber)
            //pass the first text view to the viewModel after creation for the navigation of the links in the first page
            if (page.pageNumber == 1) {
                viewModel._textView.value = textView
            }
            Log.d("PagesScreen", "page number = ${textView.pageNumber}")
            return@AndroidView view
        },
        update = { view ->
            //this block executed whenever an update happens to the textView
            val textView = view.findViewById<IHTextView>(R.id.ihTextView)
            //if the textView is the target page of a link:
            if (viewModel.linkNavigationPage.value) {
                viewModel._textView.value = textView
                viewModel._linkNavigationPage.value = false
            }
            //this fun updates the text styles of the text view whenever any of these values change
            updateTextViewStyle(textView, fontSize, fontColor, fontWeight)
        }
    )
}