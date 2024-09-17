package ih.tools.readingpad.util

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel

class IHNoteSpan() : IHSpan {
    override var id: Long = 0

    constructor(id: Long) : this() {
        this.id = id
    }
}

class IHNoteClickableSpan(
    val viewModel: BookContentViewModel,
    val uiStateViewModel: UIStateViewModel,
    val text: String
) : ClickableSpan(), IHSpan {

    override var id: Long = 0
    var noteText = text

    constructor(
        id: Long,
        text: String,
        viewModel: BookContentViewModel,
        uiStateViewModel: UIStateViewModel
    ) : this(viewModel, uiStateViewModel, text) {
        this.id = id
        this.noteText = text
    }

    override fun onClick(p0: View) {
//        viewModel.setNoteClickEvent(this)
//        viewModel.setEditNote(true)
        uiStateViewModel.setNoteClickEvent(this)
        uiStateViewModel.setEditNote(true)
    }

    override fun updateDrawState(ds: TextPaint) {
        //super.updateDrawState(ds)
    }
}