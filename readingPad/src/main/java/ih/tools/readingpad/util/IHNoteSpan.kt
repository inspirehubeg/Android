package ih.tools.readingpad.util

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel

class IHNoteSpan(
    val viewModel: BookContentViewModel,
    val text: String
) :  ClickableSpan(), IHSpan {

    override var id: Long = 0
     var noteText = text

    constructor(id: Long, text: String, viewModel: BookContentViewModel) : this(viewModel, text) {
        this.id = id
        this.noteText = text
    }

    override fun onClick(p0: View) {
        viewModel.setNoteClickEvent(this)
        viewModel.setEditNote(true)
    }

    override fun updateDrawState(ds: TextPaint) {
       // super.updateDrawState(ds)
    }
}