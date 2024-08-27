package ih.tools.readingpad.feature_book_parsing.presentation.text_view


import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.util.Log
import android.view.ActionMode
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkClickableSpan
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkImageSpan
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.util.IHBackgroundSpan
import ih.tools.readingpad.util.IHSpan
import ih.tools.readingpad.util.copyTextWithSignature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt
import kotlin.properties.Delegates


class IHTextView : AppCompatTextView, View.OnClickListener, View.OnTouchListener {
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context!!, attrs, defStyleAttr) {
        Log.d("IHTextView", "1st constructor is called")
    }

    constructor(context: Context) : super(context) {
        Log.d("IHTextView", "2nd constructor is called")
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        Log.d(
            "IHTextView",
            "3rd constructor is called"
        ) //this is the constructor used by RecyclerView
        this.setOnTouchListener(this)
    }

    private lateinit var spannableString: Spannable
    private lateinit var viewModel: BookContentViewModel
    var pageNumber: Int = 1
    private var fontSize by Delegates.notNull<Float>() //by Delegates.notNull() is used to initialize the variable later instead of lateinit with primitive types

    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    override fun onCreateContextMenu(menu: ContextMenu?) {
        super.onCreateContextMenu(menu)
    }

    override fun onClick(p0: View?) {
    }

    //custom function to set text of type SpannableStringBuilder
    // and takes the viewModel needed for the highlight and bookmark functions
    fun setText(
        spannableStringBuilder: SpannableStringBuilder,
        bookContentViewModel: BookContentViewModel,
        currentPageNumber: Int
    ) {
        Log.d("IHTextView", "setText is called")

        super.setText(spannableStringBuilder)
        spannableString = this.getText().toSpannable()

        viewModel = bookContentViewModel
        fontSize = viewModel.fontSize.value
        pageNumber = currentPageNumber
        coroutineScope.launch {
            delay(200) // this delay to allow the coroutine to fetch the book bookmarks before getting the page bookmarks
            viewModel.getHighlightsForPage(pageNumber, this@IHTextView)
            viewModel.getBookmarksForPage(pageNumber, this@IHTextView)
        }

        this.customSelectionActionModeCallback =
            CustomMenu() //using custom selection menu with highlight and bookmark options
    }

    fun getDatabaseIndex(index: Int): Int {
        val tt = spannableString.getSpans(0, index, IHBookmarkImageSpan::class.java)
        return index - (tt.size *2)
    }
    fun getViewIndex(index: Int): Int {
        val tt = spannableString.getSpans(0, index, IHBookmarkImageSpan::class.java)
        return index + (tt.size *2)
    }

    //this function used by the recycler view to calculate and navigate to the correct line
    fun scrollToIndexRecycler(recyclerView: RecyclerView, index: Int) {
        val targetY = getYCoordinateForIndex(index)
        recyclerView.smoothScrollBy(0, targetY)
        Log.e("scrollToIndex", "index = $index")
        Log.e("scrollToIndex", "targetY = $targetY")
    }

    fun getYCoordinateForIndex(index: Int): Int {
      val newIndex =  getDatabaseIndex(index)
        // the target line
        val targetLine = layout.getLineForOffset(newIndex)
        // Return the Y coordinate of the top of the line
        val lineTop = layout.getLineTop(targetLine)
        return lineTop
    }


    fun drawSingleHighlight(id: Long, start: Int, end: Int, isViewIndex: Boolean) {
        if (!isViewIndex) {
            val viewStart = getViewIndex(start)
            val viewEnd = getViewIndex(end)
            spannableString.setSpan(
                IHBackgroundSpan(id),
                viewStart,
                viewEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannableString.setSpan(
                IHBackgroundSpan(id),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        text = spannableString
    }

    fun drawAllHighlights(highlights: List<Highlight>) {
        highlights.forEach { highlight ->
            Log.d("IHTextView", "highlight = ${highlight}")
            Log.d("IHTextView", "highlight.pageNumber = ${highlight.pageNumber}")
            drawSingleHighlight(highlight.id, highlight.start, highlight.end , false)
        }
    }

    /** removes a highlight or bookmark span from the text */
    fun removeSingleCustomSpan(span: IHSpan) {

        val textSpan= SpannableStringBuilder ()
        textSpan.append(spannableString)


        if(span is IHBookmarkClickableSpan){
            val spanStart = spannableString.getSpanStart(span)
//            val firstPart = spannableString.subSequence(0,spannableString.getSpanStart(span) ).toSpannable()
//            val secondPart = spannableString.subSequence(spannableString.getSpanStart(span)+2,spannableString.length).toSpannable()
            textSpan.removeSpan(span)
            textSpan.replace(spanStart-2,spanStart,"")
            spannableString = textSpan
        }
        else{
            spannableString.removeSpan(span)
        }
        text = spannableString
    }




    fun drawSingleBookmark(id: Long, name: String, start: Int, end: Int, isViewIndex: Boolean) {
        var viewStart = start
        var viewEnd = end

        if (!isViewIndex) {
             viewStart = getViewIndex(start)
             viewEnd = getViewIndex(end)
        } else{
           // var firstChar = start

            for (i in start downTo 0){
                val char = spannableString.get(i)
                if (char.isWhitespace() || char.equals("\n")){
                    viewStart = i+1
                    break
                }
            }
        }

//        val currentSpan = spannableString.getSpans<Objects>(start,end)
//        Log.d("newSpan", "currentSpan = ${currentSpan.size}")

        //val firstPart = spannableString.subSequence(0,firstChar ).toSpannable()
        //val secondPart = spannableString.subSequence(firstChar,spannableString.length).toSpannable()
        val textSpan = SpannableStringBuilder()
        textSpan.append(spannableString)
        textSpan.insert(viewStart,"y ")

        spannableString = textSpan

        val lineHeight = this.lineHeight

        val image : Drawable = if (viewModel.darkTheme.value){
            context.getDrawable(R.drawable.bookmark2)!!
        }else context.getDrawable(R.drawable.bookmark1)!!
        val bitmapDrawable = image.toBitmap().let { bitmap->
            BitmapDrawable(resources, bitmap)
        }
        bitmapDrawable.setBounds(0,0,(lineHeight * .7).toInt() ,lineHeight)
        val imageSpan = IHBookmarkImageSpan( bitmapDrawable, id)

        spannableString.setSpan(
            imageSpan,
            viewStart ,
            viewStart +1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        val span = IHBookmarkClickableSpan(id = id, name = name, viewModel = viewModel)
        viewModel.bookmarkSpans[id] = span
        spannableString.setSpan(
            span,
            viewStart +2,
            viewEnd+2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        Log.d("rasm", "drawSingleBookmark is called, start = $start, end = $end")
        text = spannableString
    }


    /** when a part of the text is selected this checks if it contains a highlight or bookmark span
     *  to be able to remove them*/
    var selectedSpans: Array<IHSpan> = emptyArray()

    private fun getBackgroundSpans(start: Int, end: Int): Array<IHSpan> {
        val margin = 0
        var spanStart = 0
        var spanEnd = 0

        if (start > margin) {
            spanStart = start - margin
        }
        if (end < spannableString.length - margin) {
            spanEnd = end + margin
        }
        Log.d("IHTextView", "spanStart = $spanStart $spanEnd")

        selectedSpans = spannableString.getSpans(spanStart, spanEnd, IHSpan::class.java)

        for (i in selectedSpans) {
            val j = spannableString.getSpanStart(i)
            val k = spannableString.getSpanEnd(i)
            Log.d("getHighlightSpans", "id = ${i.id}, start = $j, end = $k")
        }
        return selectedSpans
    }
//    private var isScrolling = false
//    override fun onScrollChanged(horiz: Int, vert: Int, oldHoriz: Int, oldVert: Int) {
//        super.onScrollChanged(horiz, vert, oldHoriz, oldVert)
//        //viewModel._showTopBar.value = false
//        if (!isScrolling) {
//            // User has started scrolling
//            isScrolling = true
//
//        }
//    }
    private var scale = 1f
    private var initialPointerDistance = 0f
    private var pressStart: Int = 0
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            Log.d("onTouch", "onTouch is called")
            //whenever a touch happens on the screen the current text view is sent to the viewModel
            // to allow the highlight and bookmark functions in that exact text view
            viewModel.setTextView(this)

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    pressStart = this.getOffsetForPosition(event.x, event.y)
                    // Handle pinch start
//                    if (event.pointerCount == 2) {
//                        initialPointerDistance = distance(event)
//                    }
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    // Reset initialPointerDistance when a finger is lifted
                    initialPointerDistance = 0f
                    }

                MotionEvent.ACTION_MOVE -> {
                    // Handle pinch gesture
                    if (event.pointerCount == 2) {
                        if (initialPointerDistance == 0f) {
                            Log.d("twoFingers", "initialPointerDistance is 0")
                            initialPointerDistance = distance(event) // Initialize only on first move
                        }
                        val currentDistance = distance(event)
                        val newScale = scale * (currentDistance / initialPointerDistance)
                        if (newScale != scale) {
                            scale = newScale
                            val minScale = 12f / fontSize // Minimum scale for font size 12
                            val maxScale = 32f / fontSize // Maximum scale for font size 32
                            scale = scale.coerceIn(minScale, maxScale) // Adjust zoom limits

                            val newFontSize = fontSize * scale
                            textSize = newFontSize // Update text size
                            viewModel.setFontSize(newFontSize) // Save the updated font size
                            initialPointerDistance = currentDistance
                        }
                    }
                }

                MotionEvent.ACTION_UP -> {
                    //isScrolling = false
                    val pressEnd = this.getOffsetForPosition(event.x, event.y)
                    val start = minOf(pressStart, pressEnd)
                    val end = maxOf(pressStart, pressEnd)

                    // Check for a quick tap with no text selection to make the bars appear or disappear
                    if (event.eventTime - event.downTime < 300 && !hasSelection()) {
                        Log.d("onTouch", "quick tap")
                        viewModel.setTopBarVisibility(!viewModel.showTopBar.value)
                    } else {
                        // Handle span selection
                        Log.d("onTouch", "selection")
                        getBackgroundSpans(start, end)
                    }
                }

                MotionEvent.ACTION_CANCEL -> {
                    //isScrolling = false
                }
                MotionEvent.ACTION_SCROLL ->{
                    Log.d("move", "move is called")
                }
            }
        }
        return false
    }
    //calculate the distance between two fingers
    private fun distance(event: MotionEvent): Float {
        val x1 = event.getX(0)
        val y1 = event.getY(0)
        val x2 = event.getX(1)
        val y2 = event.getY(1)
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))
    }

    inner class CustomMenu : ActionMode.Callback {
        override fun onCreateActionMode(
            mode: ActionMode,
            menu: Menu
        ): Boolean {
            // Inflate your custom menu
            mode.menuInflater.inflate(
                R.menu.custom_selection_menu,
                menu
            )
            return true
        }

        override fun onPrepareActionMode(
            mode: ActionMode,
            menu: Menu
        ): Boolean {

            val highLightSpans = selectedSpans.filterIsInstance<IHBackgroundSpan>()
            val bookmarkSpans = selectedSpans.filterIsInstance<IHBookmarkClickableSpan>()
            if (highLightSpans.isEmpty()) {
                menu.findItem(R.id.action_highlight)?.isVisible = true
                menu.findItem(R.id.action_remove_highlight)?.isVisible = false
            } else {
                menu.findItem(R.id.action_highlight)?.isVisible = false
                menu.findItem(R.id.action_remove_highlight)?.isVisible = true
            }

            if (bookmarkSpans.isEmpty()) {
                menu.findItem(R.id.action_bookmark)?.isVisible = true
                menu.findItem(R.id.action_remove_bookmark)?.isVisible = false
            } else {
                menu.findItem(R.id.action_bookmark)?.isVisible = false
                menu.findItem(R.id.action_remove_bookmark)?.isVisible = true
            }

            // Remove default menu items
            menu.removeItem(android.R.id.cut)
            menu.removeItem(android.R.id.shareText)
            menu.removeItem(android.R.id.paste)
            menu.removeItem(android.R.id.selectAll)

            return true
        }

        override fun onActionItemClicked(
            mode: ActionMode,
            item: MenuItem
        ): Boolean {
            when (item.itemId) {
                android.R.id.copy -> {
                    val selectedText =
                        text.subSequence(
                            selectionStart,
                            selectionEnd
                        )
                    Log.d(
                        "BookContentScreen",
                        "Selected text length: ${selectedText.length}"
                    )

                    val truncatedText = selectedText.take(150)
                    Log.d("BookContentScreen", "truncated text length: ${truncatedText.length}")
                    // Handle copying or sharing the truncatedText
                    copyTextWithSignature(truncatedText, context)

                    return true
                }

                //if selection range doesn't contain highlight, show highlight action
                R.id.action_highlight -> {
                    // Get the start and end positions of the selected text
                    val start = selectionStart
                    val end = selectionEnd
                    if (start != end) {
                        // Add the highlight to the TextView
                        viewModel.addHighlight(pageNumber = pageNumber, start, end, this@IHTextView)
                        return true
                    }
                }

                //else if selection range contains highlight, show remove highlight action
                R.id.action_remove_highlight -> {
                    val start = selectionStart
                    val end = selectionEnd
                    val selectionSpans =
                        if (start != end) {
                            getBackgroundSpans(start, end)
                        } else {
                            emptyArray()
                        }
                    val highlightSpans = selectionSpans.filterIsInstance<IHBackgroundSpan>()

                    if (highlightSpans.isNotEmpty()) {
                        highlightSpans.forEach { span ->
                            viewModel.removeHighlightById(span.id)
                            removeSingleCustomSpan(span)
                        }
                    }
                    // viewModel.getHighlightsForPage(pageNumber, this@IHTextView)
                    return true
                }

                R.id.action_remove_bookmark -> {
                    val start = selectionStart
                    val end = selectionEnd
                    val selectionSpans =
                        if (start != end) {
                            getBackgroundSpans(start, end)
                        } else {
                            emptyArray()
                        }
                    val bookmarkSpans = selectionSpans.filterIsInstance<IHBookmarkClickableSpan>()

                    if (bookmarkSpans.isNotEmpty()) {
                        bookmarkSpans.forEach { span ->
                            viewModel.removeBookmarkById(span.id.toLong())
                            viewModel.setTextView(this@IHTextView)
                            //removeSingleCustomSpan(span)
                        }
                    }
                    //viewModel.getPageBookmarks(pageNumber, this@IHTextView)
                    return true

                }

                R.id.action_bookmark -> {
                    val start = selectionStart
                    val end = selectionEnd

                    if (start != end) {
                        val bookmarkSpan = IHBookmarkClickableSpan(viewModel = viewModel)
                        //opens the dialog in the ui so that the user can add a name then save the bookmark to db
                        viewModel.setTextView(this@IHTextView)
                        viewModel.setBookmarkClickEvent(bookmarkSpan)
                        viewModel.setBookmarkStart(start)
                        viewModel.setBookmarkEnd(end)
                        viewModel.setBookmarkPageNumber(pageNumber)


                        // viewModel.addBookmark(pageNumber = pageNumber, start, end, this@IHTextView)

                        // Provide feedback to the user (e.g., toast message)
//                        showToast(
//                            context = context,
//                            "bookmark_added_successfully"
//                        )
                    }
                    return true
                }
            }
            return false
        }

        override fun onDestroyActionMode(p0: ActionMode?) {

        }
    }
}





