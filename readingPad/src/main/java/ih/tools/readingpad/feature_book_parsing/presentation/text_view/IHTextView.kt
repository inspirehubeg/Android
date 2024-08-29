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


/**
 * A custom TextView that provides interactive features like highlighting, bookmarking, and text scaling.
 *It extends AppCompatTextView and implements OnClickListener and OnTouchListener for handling user interactions.
 */
class IHTextView : AppCompatTextView, View.OnClickListener, View.OnTouchListener {

    /**
     * Constructor for use in code.
     *
     * @param context The Context the view is running in.
     */
    constructor(context: Context) : super(context) {
        Log.d("IHTextView", "2nd constructor is called")
    }

    /**
     * Constructor that is called when inflating a view from XML.
     *
     * @param context The Context the view is running in.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        Log.d(
            "IHTextView", "3rd constructor is called"
        ) //this is the constructor used by RecyclerView
        this.setOnTouchListener(this)
    }

    /**
     * Perform inflation from XML and apply a class-specific base style.
     *
     * @param context The Context the view is running in.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context!!, attrs, defStyleAttr) {
        Log.d("IHTextView", "1st constructor is called")
    }

    private lateinit var spannableString: Spannable
    private lateinit var viewModel: BookContentViewModel
    var pageNumber: Int = 1
    private var fontSize by Delegates.notNull<Float>() //by Delegates.notNull() is used to initialize the variable later instead of late init with primitive types

    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    override fun onCreateContextMenu(menu: ContextMenu?) {
        super.onCreateContextMenu(menu)
    }

    override fun onClick(p0: View?) {
        // Not used currently
    }
    /**
     * Sets the text of the TextView and initializes interactive features.
     *
     * @param spannableStringBuilder The text to display, as a SpannableStringBuilder.
     * @param bookContentViewModel The ViewModel associated with the book content.
     * @param currentPageNumber The current page number.
     */
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

    /**
     * Calculates the index in the database based on the view index, considering bookmark spans.
     *
     * @param index The index in the view.
     * @return The corresponding index in the database.
     */

    fun getDatabaseIndex(index: Int): Int {
        val bookmarkSpans = spannableString.getSpans(0, index, IHBookmarkImageSpan::class.java)
        return index - (bookmarkSpans.size * 2)
    }

    /**
     * Calculates the index in the view based on the database index, considering bookmark spans.
     *
     * @param index The index in the database.
     * @return The corresponding index in the view.
     */
    private fun getViewIndex(index: Int): Int {
        val bookmarkSpans = spannableString.getSpans(0, index, IHBookmarkImageSpan::class.java)
        return index + (bookmarkSpans.size * 2)
    }

    /**
     * Scrolls the RecyclerView to the specified index.
     *
     * @param recyclerView The RecyclerView to scroll.
     * @param index The index to scroll to.
     */
    fun scrollToIndexRecycler(recyclerView: RecyclerView, index: Int) {
        val targetY = getYCoordinateForIndex(index)
        recyclerView.smoothScrollBy(0, targetY)
        Log.e("scrollToIndex", "index = $index")
        Log.e("scrollToIndex", "targetY = $targetY")
    }

    /**
     * Calculates the Y coordinate for the specified index.
     *
     * @param index The index.
     * @return The Y coordinate.
     */
    fun getYCoordinateForIndex(index: Int): Int {
        val newIndex = getDatabaseIndex(index)
        // the target line
        val targetLine = layout.getLineForOffset(newIndex)
        // Return the Y coordinate of the top of the line
        val lineTop = layout.getLineTop(targetLine)
        return lineTop
    }


    /**
     * Draws a single highlight span on the text.
     *
     * @param id The ID of the highlight.
     * @param start The start index of the highlight.
     * @param end The end index of the highlight.
     * @param isViewIndex True if the indices are view indices, false if they are database indices.
     */
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

    /**
     * Draws all highlights from the provided list.
     *
     * @param highlights The list of highlights to draw.
     */
    fun drawAllHighlights(highlights: List<Highlight>) {
        highlights.forEach { highlight ->
            Log.d("IHTextView", "highlight = $highlight")
            Log.d("IHTextView", "highlight.pageNumber = ${highlight.pageNumber}")
            drawSingleHighlight(highlight.id, highlight.start, highlight.end, false)
        }
    }

    /**
     * Removes a single custom span (highlight or bookmark) from the text.
     *
     * @param span The span to remove.
     */
    fun removeSingleCustomSpan(span: IHSpan) {
        val textSpan = SpannableStringBuilder()
        textSpan.append(spannableString)

        if (span is IHBookmarkClickableSpan) {
            val spanStart = spannableString.getSpanStart(span)
            textSpan.removeSpan(span)
            if (spanStart >= 2)
                textSpan.replace(spanStart - 2, spanStart, "")
            spannableString = textSpan
        } else {
            spannableString.removeSpan(span)
        }
        text = spannableString
    }


    /**
     * Draws a single bookmark on the text.
     *
     * @param id The ID of the bookmark.
     * @param name The name of the bookmark.
     * @param start The start index of the bookmark.
     * @param end The end index of the bookmark.
     * @param isViewIndex True if the indices are view indices, false if they are database indices.
     */
    fun drawSingleBookmark(id: Long, name: String, start: Int, end: Int, isViewIndex: Boolean) {
        var viewStart = start
        var viewEnd = end

        if (!isViewIndex) {
            viewStart = getViewIndex(start)
            viewEnd = getViewIndex(end)
        } else {
            // var firstChar = start

            for (i in start downTo 0) {
                val char = spannableString[i]
                if (char.isWhitespace() || char == ('\n')) {
                    viewStart = i + 1
                    break
                }
            }
        }

        val textSpan = SpannableStringBuilder()
        textSpan.append(spannableString)
        textSpan.insert(viewStart, "y ") // Placeholder for the bookmark image
        spannableString = textSpan

        val image: Drawable = if (viewModel.darkTheme.value) {
            context.getDrawable(R.drawable.bookmark2)!!
        } else context.getDrawable(R.drawable.bookmark1)!!
        val bitmapDrawable = BitmapDrawable(resources, image.toBitmap())

        bitmapDrawable.setBounds(0, 0, (lineHeight * .7).toInt(), lineHeight)
        val imageSpan = IHBookmarkImageSpan(bitmapDrawable, id)

        spannableString.setSpan(
            imageSpan,
            viewStart,
            viewStart + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        val span = IHBookmarkClickableSpan(id = id, name = name, viewModel = viewModel)
        viewModel.bookmarkSpans[id] = span
        spannableString.setSpan(
            span,
            viewStart + 2,
            viewEnd + 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        Log.d("drawSingleBookmark", "drawSingleBookmark is called, start = $start, end = $end")
        text = spannableString
    }


    /** The spans (highlights or bookmarks) that are currently selected. */
    var selectedSpans: Array<IHSpan> = emptyArray()

    /**
     * Retrieves the background spans (highlights or bookmarks) within the specified range.
     *
     * @param start The start index of the range.
     * @param end The end index of the range.
     * @return An array of IHSpan objects representing the spans within the range.
     */
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

        for (span in selectedSpans) {
            val backgroundSpanStart = spannableString.getSpanStart(span)
            val backgroundSpanEnd = spannableString.getSpanEnd(span)
            Log.d(
                "getHighlightSpans",
                "id = ${span.id}, start = $backgroundSpanStart, end = $backgroundSpanEnd"
            )
        }
        return selectedSpans
    }

    private var scale = 1f
    private var initialPointerDistance = 0f
    private var pressStart: Int = 0

    /**
     * Handles touch events on the TextView.
     * This method manages text scaling using pinch gestures and detects span selections.
     *
     * @param view The view that was touched.
     * @param event The MotionEvent object containing full information about the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            Log.d("onTouch", "onTouch is called")
            //whenever a touch happens on the screen the current text view is sent to the viewModel
            // to allow the highlight and bookmark functions in that exact text view
            viewModel.setTextView(this)

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    pressStart = this.getOffsetForPosition(event.x, event.y)
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
                            initialPointerDistance =
                                distance(event) // Initialize only on first move
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
                            if (viewModel.fontSizeChanged.value) {
                                viewModel.scrollToIndex(pageNumber - 1)
                                viewModel.setFontSizeChanged(false)
                            }
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
                    // Do nothing
                }

                MotionEvent.ACTION_SCROLL -> {
                    Log.d("move", "move is called")
                }
            }
        }
        return false
    }

    /**
     * Calculates the distance between two pointer touch events.
     *
     * @param event The MotionEvent containing the touch events.
     * @return The distance between the two pointers.
     */
    private fun distance(event: MotionEvent): Float {
        val x1 = event.getX(0)
        val y1 = event.getY(0)
        val x2 = event.getX(1)
        val y2 = event.getY(1)
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))
    }


    /**
     * A custom ActionMode.Callback for handling the contextual action bar that appears when text is selected.
     * Provides options for highlighting, bookmarking, removing highlights/bookmarks, and copying text.
     */
    inner class CustomMenu : ActionMode.Callback {
        /**
         * Called when the action mode is created.
         * Inflates the custom menu for the contextual action bar.
         *
         * @param mode The ActionMode that was created.
         * @param menu The Menu of the action mode.
         * @return True if the action mode should be created, false if entering this
         *              mode should be aborted.
         */
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

        /**
         * Called each time the action mode is shown.
         * Hides/shows menu items based on whether the selection contains highlights or bookmarks.
         *
         * @param mode The ActionMode that was created.
         * @param menu The Menu of the action mode.
         * @return True if the menu or action mode was updated, false otherwise.
         */
        override fun onPrepareActionMode(
            mode: ActionMode,
            menu: Menu
        ): Boolean {

            val highLightSpans = selectedSpans.filterIsInstance<IHBackgroundSpan>()
            val bookmarkSpans = selectedSpans.filterIsInstance<IHBookmarkClickableSpan>()

            // Show/hide highlight actions based on selection
            if (highLightSpans.isEmpty()) {
                menu.findItem(R.id.action_highlight)?.isVisible = true
                menu.findItem(R.id.action_remove_highlight)?.isVisible = false
            } else {
                menu.findItem(R.id.action_highlight)?.isVisible = false
                menu.findItem(R.id.action_remove_highlight)?.isVisible = true
            }

            // Show/hide bookmark actions based on selection
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

        /**
         * Called when the user selects an action item from the contextual action bar.
         * Handles actions for copying, highlighting, removing highlights, bookmarking, and removing bookmarks.
         *
         * @param mode The ActionMode that was created.
         * @param item The menu item that was clicked.
         * @return True if the callback handled the event, false if the system should proceed
         */
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
                            viewModel.removeBookmarkById(span.id)
                            viewModel.setTextView(this@IHTextView)
                        }
                    }
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
                    }
                    return true
                }
            }
            return false
        }

        /**
         * Called when the action mode is destroyed.
         *
         * @param mode The ActionMode that was destroyed.
         */
        override fun onDestroyActionMode(mode: ActionMode?) {

        }
    }
}





