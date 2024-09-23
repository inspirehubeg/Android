package ih.tools.readingpad.feature_book_parsing.presentation.text_view


import android.content.Context
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntOffset
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkClickableSpan
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkSpan
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.ui.UIStateViewModel
import ih.tools.readingpad.util.BOOKMARK_ICON
import ih.tools.readingpad.util.IHBackgroundSpan
import ih.tools.readingpad.util.IHNoteClickableSpan
import ih.tools.readingpad.util.IHNoteSpan
import ih.tools.readingpad.util.IHSpan
import ih.tools.readingpad.util.NOTE_ICON
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
    private lateinit var uiStateViewModel: UIStateViewModel
    var pageNumber: Int = 1
    var chapterNumber: Int = 1
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
        stateViewModel: UIStateViewModel,
        currentPageNumber: Int,
        currentChapterIndex: Int
    ) {
        Log.d("IHTextView", "setText is called")

        super.setText(spannableStringBuilder)
        spannableString = this.getText().toSpannable()

        viewModel = bookContentViewModel
        uiStateViewModel = stateViewModel
        //fontSize = viewModel.fontSize.value
        fontSize = stateViewModel.uiSettings.value.fontSize
        pageNumber = currentPageNumber
        chapterNumber = currentChapterIndex + 1
        if (stateViewModel.uiSettings.value.showHighlightsBookmarks) {
            coroutineScope.launch {
                delay(200) // this delay to allow the coroutine to fetch the book bookmarks before getting the page bookmarks
                viewModel.getHighlightsForPage(pageNumber, this@IHTextView)
                viewModel.getBookmarksForPage(pageNumber, this@IHTextView)
                viewModel.getNotesForPage(pageNumber, this@IHTextView)
            }
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
        val bookmarkSpans = spannableString.getSpans(0, index, IHBookmarkClickableSpan::class.java)
        val noteSpans = spannableString.getSpans(0, index, IHNoteClickableSpan::class.java)
        return index - ((bookmarkSpans.size * 3) + (noteSpans.size * 2))
    }

    /**
     * Calculates the index in the view based on the database index, considering bookmark spans.
     *
     * @param index The index in the database.
     * @return The corresponding index in the view.
     */
    private fun getViewIndex(index: Int): Int {
        val bookmarkSpans = spannableString.getSpans(0, index, IHBookmarkClickableSpan::class.java)
        val noteSpans = spannableString.getSpans(0, index, IHNoteClickableSpan::class.java)

        return index + ((bookmarkSpans.size * 3) + (noteSpans.size * 2))
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
        Log.d("layout", "${layout}")
        val targetLine = layout.getLineForOffset(newIndex)
        // Return the Y coordinate of the top of the line
        val lineTop = layout.getLineTop(targetLine)
        return lineTop
    }

    fun getTopPageIndex(): Int {
        val y = scrollY

        return y
    }


    /**
     * Draws a single highlight span on the text.
     *
     * @param id The ID of the highlight.
     * @param start The start index of the highlight.
     * @param end The end index of the highlight.
     * @param isViewIndex True if the indices are view indices, false if they are database indices.
     */
    fun drawSingleHighlight(id: Long, start: Int, end: Int, isViewIndex: Boolean, color: Int) {
        if (!isViewIndex) {
            val viewStart = getViewIndex(start)
            val viewEnd = getViewIndex(end)
            spannableString.setSpan(
                IHBackgroundSpan(id, color),
                viewStart,
                viewEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannableString.setSpan(
                IHBackgroundSpan(id, color),
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
            drawSingleHighlight(
                highlight.id,
                highlight.start,
                highlight.end,
                false,
                color = highlight.color
            )
        }
    }

    fun removeBookmarkSpans(bookmarkId: Long) {

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
            if (spanStart >= 3)
                textSpan.replace(spanStart, spanStart + 3, "")
            spannableString = textSpan
        } else if (span is IHNoteClickableSpan) {
            val spanStart = spannableString.getSpanStart(span)
            textSpan.removeSpan(span)
            if (spanStart >= 2)
                textSpan.replace(spanStart, spanStart + 2, "")
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
            for (i in start downTo 0) {
                val char = spannableString[i]
                if (char == ('\n')) {
                    viewStart = i + 1
                    break
                }
            }
            for (i in end until spannableString.length) {
                val char = spannableString[i]
                if (char == ('\n')) {
                    viewEnd = i - 1
                    break
                }
            }
        }


        //val bookmarkIcon = "\uD83C\uDFF7\uFE0F"
        val textSpan = SpannableStringBuilder()
        textSpan.append(spannableString)
        textSpan.insert(viewStart, BOOKMARK_ICON) // Placeholder for the bookmark image
        spannableString = textSpan

        val clickableSpan = IHBookmarkClickableSpan(
            id = id,
            name = name,
            viewModel = viewModel,
            uiStateViewModel = uiStateViewModel
        )

        spannableString.setSpan(
            clickableSpan,
            viewStart,
            viewStart + 3,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val bookmarkSpan = IHBookmarkSpan(id = id)
        viewModel.bookmarkClickableSpans[id] = clickableSpan
        viewModel.bookmarkSpans[id] = bookmarkSpan
        spannableString.setSpan(
            bookmarkSpan,
            viewStart,
            viewEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        Log.d("drawSingleBookmark", "drawSingleBookmark is called, start = $start, end = $end")
        text = spannableString
    }


    fun drawSingleNote(id: Long, noteText: String, start: Int, end: Int, isViewIndex: Boolean) {
        var viewStart = start
        var viewEnd = end
        if (!isViewIndex) {
            viewStart = getViewIndex(start)
            viewEnd = getViewIndex(end)
        }

        val textSpan = SpannableStringBuilder()
        textSpan.append(spannableString)
        textSpan.insert(viewStart, NOTE_ICON) // Placeholder for the bookmark image
        spannableString = textSpan

        val span = IHNoteClickableSpan(
            id = id,
            text = noteText,
            viewModel = viewModel,
            uiStateViewModel = uiStateViewModel
        )
        viewModel.noteClickableSpans[id] = span
        spannableString.setSpan(
            span,
            viewStart,
            viewStart + 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val noteSpan = IHNoteSpan(id = id)
        viewModel.noteSpans[id] = noteSpan
        spannableString.setSpan(
            noteSpan,
            viewStart,
            viewEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        Log.d("drawSingleBookmark", "drawSingleBookmark is called, start = $start, end = $end")
        text = spannableString
    }


    /** The spans (highlights or bookmarks) that are currently selected. */
    //val selectedSpans = viewModel.selectedSpans.value

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

        val selectedSpans = spannableString.getSpans(spanStart, spanEnd, IHSpan::class.java)
        viewModel.setSelectedSpans(selectedSpans.toList())
        for (span in selectedSpans) {
            Log.d("getBackgroundSpans", "selectedSpans = ${span.id}")
        }

        return selectedSpans
    }

    private var scale = 1f
    private var initialPointerDistance = 0f
    var pressStart = -1

    var itemKey: Any? = null // Adda property to store the item key

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
                    Log.d("test", "inside ActionUp")
                    // Reset initialPointerDistance when a finger is lifted
                    initialPointerDistance = 0f
                }

                MotionEvent.ACTION_MOVE -> {
//                    // Handle pinch gesture
                    if (event.pointerCount == 2) {
//                        if (viewModel.oneFingerScroll.value) {
//                            viewModel.setOneFingerScroll(false)
//                        }
                        if (initialPointerDistance == 0f) {
                            Log.d("twoFingers", "initialPointerDistance is 0")
                            initialPointerDistance =
                                distance(event) // Initialize only on first move
                        }
                        val currentDistance = distance(event)
//                        if (currentDistance - initialPointerDistance < 3f) {
//                            Log.d(
//                                "IHTextView",
//                                "distance is too small = ${currentDistance - initialPointerDistance}"
//                            )
//                            return false
//                        }
                        val newScale = scale * (currentDistance / initialPointerDistance)
                        if (newScale != scale) {
                            scale = newScale
                            val minScale = 12f / fontSize // Minimum scale for font size 12
                            val maxScale = 32f / fontSize // Maximum scale for font size 32
                            scale = scale.coerceIn(minScale, maxScale) // Adjust zoom limits
                            val newFontSize = fontSize * scale
                            textSize = newFontSize // Update text size
                            uiStateViewModel.setFontSize(
                                newFontSize,
                                event.y
                            ) // Save the updated font size
                            if (uiStateViewModel.fontSizeChanged.value) {
                                //viewModel.scrollToIndex(pageNumber - 1)
                                uiStateViewModel.setFontSizeChanged(false)
                            }
                            initialPointerDistance = currentDistance
//                    }
//                    else if (viewModel.oneFingerScroll.value == false){
//                        //viewModel.setOneFingerScroll(true)
//                    }
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
                        // viewModel.setTopBarVisibility(!viewModel.showTopBar.value)
                        if (!uiStateViewModel.uiSettings.value.pinnedTopBar){
                            Log.d("onTouch", "topBar not pinned")
                            uiStateViewModel.toggleTopBar(!uiStateViewModel.showTopBar.value)
                        }

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

    fun copyText() {
        val selectedText =
            text.subSequence(
                selectionStart,
                selectionEnd
            )

        val truncatedText = selectedText.take(150)
        Log.d("BookContentScreen", "truncated text length: ${truncatedText.length}")
        // Handle copying or sharing the truncatedText
        copyTextWithSignature(truncatedText, context)
    }

    fun addBookmarkOnText() {
        val start = selectionStart
        var viewStart: Int = start
        for (i in start downTo 0) {
            val char = spannableString[i]
            if (char == ('\n')) {
                viewStart = i + 1
                break
            }
        }
        val end = selectionEnd
        var viewEnd: Int = end
        for (i in end until spannableString.length) {
            val char = spannableString[i]
            if (char == ('\n')) {
                viewEnd = i - 1
                break
            }
            val text = spannableString.substring(start, end)
                .replace(NOTE_ICON, "")
                .replace(BOOKMARK_ICON, "")


            val words = text.split(' ')
            Log.d("filterWords", "filteredWords = $words")

            val bookmarkName = if (words.size >= 3) {
                words.subList(0, 3).joinToString(" ")
            } else words.joinToString(" ")

            if (start != end) {
                val bookmarkClickableSpan =
                    IHBookmarkClickableSpan(
                        viewModel = viewModel,
                        name = bookmarkName,
                        uiStateViewModel = uiStateViewModel
                    )
                //opens the dialog in the ui so that the user can add a name then save the bookmark to db
                viewModel.setTextView(this@IHTextView)
                //viewModel.setBookmarkClickEvent(bookmarkClickableSpan)
                uiStateViewModel.setBookmarkClickEvent(bookmarkClickableSpan)
                uiStateViewModel.setBookmarkData( // Use uiStateViewModel for state
                    viewStart,
                    bookmarkName,
                    viewEnd,
                    pageNumber
                )
//                viewModel.setBookmarkStart(viewStart)
//                viewModel.setBookmarkName(bookmarkName)
//                viewModel.setBookmarkEnd(viewEnd)
//                viewModel.setBookmarkPageNumber(pageNumber)
            }
        }
    }

    fun addHighlightOnText() {
        val start = selectionStart
        val end = selectionEnd

        val text = spannableString.substring(start, end)
            .replace(NOTE_ICON, "")
            .replace(BOOKMARK_ICON, "")
        val words = text.split(' ')
        val highlightText = if (words.size >= 3) {
            words.subList(0, 3).joinToString(" ")
        } else words.joinToString(" ")
        if (start != end) {
            // Add the highlight to the TextView
            viewModel.addHighlight(
                pageNumber = pageNumber,
                start,
                end,
                this@IHTextView,
                highlightText,
                color = uiStateViewModel.preferredHighlightColor.value.toArgb()
            )
        }
    }

    fun removeHighlightOnText() {
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
    }

    fun addNoteOnText() {
        val start = selectionStart
        val end = selectionEnd
        if (start != end) {
            val noteSpan =
                IHNoteClickableSpan(
                    viewModel = viewModel,
                    uiStateViewModel = uiStateViewModel,
                    text = ""
                )
            //opens the dialog in the ui so that the user can add a name then save the note to db
            viewModel.setTextView(this@IHTextView)
            // viewModel.setNoteClickEvent(noteSpan)
            uiStateViewModel.setNoteClickEvent(noteSpan)
            uiStateViewModel.setNoteData( // Use uiStateViewModel for state
                start,
                end,
                pageNumber
            )
//            viewModel.setNoteStart(start)
//            viewModel.setNoteEnd(end)
//            viewModel.setNotePageNumber(pageNumber)
        }
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

            uiStateViewModel.toggleTopBar(true)
            uiStateViewModel.setShowCustomSelectionMenu(true)
            viewModel.setTextView(this@IHTextView)
            uiStateViewModel.setTextView(this@IHTextView)
            uiStateViewModel.setSelectionStart(selectionStart)
            uiStateViewModel.setSelectionEnd( selectionEnd)

            if (selectionStart != -1 && selectionEnd != -1) {
                val midPoint = (selectionStart + selectionEnd) / 2    //the middle of the selected text
                val horizontalPosition = layout.getPrimaryHorizontal(midPoint) //the horizontal position of the middle of the selected text

                val menuX = if (textDirection == View.TEXT_DIRECTION_RTL) {
                    //the start of the menu differs in different directions so requires a calculation
                    ((horizontalPosition - uiStateViewModel.menuWidth.value).toInt()) - paddingStart
                } else {
                    ((horizontalPosition - uiStateViewModel.menuWidth.value / 2).toInt()) + paddingStart
                }
                 // limiting the menu to the screen borders
                val menuRightEdge = menuX + uiStateViewModel.menuWidth.value
                val exceedsRightBound = menuRightEdge > uiStateViewModel.screenWidth.value
                val exceedsLeftBound = menuX < 0

                val finalMenuX = when {
                    exceedsRightBound -> (uiStateViewModel.screenWidth.value - uiStateViewModel.menuWidth.value).toInt()
                    exceedsLeftBound -> 0
                    else -> menuX
                }

                val layout = layout
                val line = layout.getLineForOffset(selectionStart)
                val y = layout.getLineTop(line)
                uiStateViewModel.setMenuPosition(IntOffset(finalMenuX, y))
            }

            menu.clear()
            return false
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
                R.id.action_copy -> {
                    copyText()
                    return true
                }

                R.id.action_yellow_highlight -> {
                    addHighlightOnText()
                    return true
                }

                R.id.action_remove_highlight -> {
                    removeHighlightOnText()
                    return true
                }

                R.id.action_add_note -> {
                    addNoteOnText()
                    return true
                }

                R.id.action_bookmark -> {
                    addBookmarkOnText()
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
            uiStateViewModel.toggleTopBar(true)
            uiStateViewModel.setShowCustomSelectionMenu(false)
            uiStateViewModel.clearSelection()
        }
    }
}


//var pressStart: Int = -1
