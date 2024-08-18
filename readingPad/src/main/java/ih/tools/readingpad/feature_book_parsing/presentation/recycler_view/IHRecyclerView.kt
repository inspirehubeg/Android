package ih.tools.readingpad.feature_book_parsing.presentation.recycler_view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class IHRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    fun scrollToPage(pageIndex: Int) {
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        smoothScroller.targetPosition = pageIndex
        layoutManager?.startSmoothScroll(smoothScroller)
    }
}
