package ih.tools.readingpad.feature_bookmark.presentation

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View

import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.util.IHSpan


class IHBookmarkClickableSpan(
    val viewModel: BookContentViewModel,
) : ClickableSpan(), IHSpan {
    var bookmarkName: String = ""
    override var id: Long = 0


    constructor(id: Long, name: String, viewModel: BookContentViewModel) : this(viewModel) {
        this.id = id
        this.bookmarkName = name
    }


    override fun updateDrawState(ds: TextPaint) {
        // super.updateDrawState(ds)
        // ds.isUnderlineText
        if (viewModel.darkTheme.value) {
            ds.setShadowLayer(3F, 0F, 10F, Color.RED)
        } else
            ds.setShadowLayer(5F, 0F, 0F, Color.RED)
    }

    override fun onClick(widget: View) {
        // on click should open a note containing the name saved
        viewModel.setBookmarkClickEvent(this)
        viewModel.setEditBookmark(true)

        Log.d("new", "onBookmarkClick is called id = $id, name = $bookmarkName")
    }


//class IHBookmarkClickableSpan (
//    val spannedText : String,
//    val viewModel: BookContentViewModel,
//
//) : DottedUnderlineSpan(_color = Color.MAGENTA, _spannedText = spannedText), IHSpan {
//
//    constructor(id: Long, spannedText: String, viewModel: BookContentViewModel) : this(spannedText, viewModel){
//        this.id = id
//    }
//
//    override fun updateDrawState(ds: TextPaint) {
//        super.updateDrawState(ds)
//    }
//    override var id: Long = 0
//    override fun getSize(
//        paint: Paint,
//        text: CharSequence,
//        start: Int,
//        end: Int,
//        fm: Paint.FontMetricsInt?
//    ): Int {
//        return super.getSize(paint, text, start, end, fm)
//    }
//
//    override fun draw(
//        canvas: Canvas,
//        text: CharSequence,
//        start: Int,
//        end: Int,
//        x: Float,
//        top: Int,
//        y: Int,
//        bottom: Int,
//        paint: Paint
//    ) {
//        super.draw(canvas, text, start, end, x, top, y, bottom, paint)
//    }


}
