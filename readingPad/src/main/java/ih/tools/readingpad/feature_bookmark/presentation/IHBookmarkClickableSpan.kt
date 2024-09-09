package ih.tools.readingpad.feature_bookmark.presentation

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.util.IHSpan


class IHBookmarkClickableSpan(
    val viewModel: BookContentViewModel,
    val name: String
) : ClickableSpan(), IHSpan {
    var bookmarkName: String = name
    override var id: Long = 0


    constructor(id: Long, name: String, viewModel: BookContentViewModel) : this(viewModel, name) {
        this.id = id
        this.bookmarkName = name
    }

    override fun onClick(widget: View) {
        // on click should open a note containing the name saved
        viewModel.setBookmarkClickEvent(this)
        viewModel.setEditBookmark(true)

        Log.d("new", "onBookmarkClick is called id = $id, name = $bookmarkName")
    }

    override fun updateDrawState(ds: TextPaint) {
    }
}
