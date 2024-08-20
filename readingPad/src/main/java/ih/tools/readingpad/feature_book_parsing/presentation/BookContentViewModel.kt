package ih.tools.readingpad.feature_book_parsing.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import book_generation.feature.book_creation.security.Decryption
import dagger.hilt.android.lifecycle.HiltViewModel
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.fetchBook
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getBookInfo
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getMetadata
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getRawFileByName
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.readFileFromRaw
import ih.tools.readingpad.feature_book_parsing.data.PreferencesManager
import ih.tools.readingpad.feature_book_parsing.domain.model.BookDetailsState
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.use_cases.BookmarkUseCases
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkClickableSpan
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.use_cases.HighlightUseCases
import ih.tools.readingpad.util.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookContentViewModel @Inject constructor(
    private val highlightUseCases: HighlightUseCases,
    private val bookmarkUseCases: BookmarkUseCases,
    private val preferencesManager: PreferencesManager,
    //private val bookmarkListener: BookmarkSpanListener,
    private val context: Context
) : ViewModel() {

    private val _imageClicked = MutableStateFlow<ByteArray?>(null)
    val imageClicked: StateFlow<ByteArray?> = _imageClicked.asStateFlow()

    val _verticalScroll = MutableStateFlow(preferencesManager.isVerticalScroll())
    val verticalScroll = _verticalScroll.asStateFlow()

    val _showBookmarkListDialog = MutableStateFlow(false)
    val showBookmarkListDialog = _showBookmarkListDialog.asStateFlow()

    val _showPageNumberDialog = MutableStateFlow(false)
    val showPageNumberDialog = _showPageNumberDialog.asStateFlow()

    val _showAddBookmarkDialog = MutableStateFlow(false)
    val showAddBookmarkDialog = _showAddBookmarkDialog.asStateFlow()


    val _showEditBookmarkDialog = MutableStateFlow(false)
    val showEditBookmarkDialog = _showEditBookmarkDialog.asStateFlow()

    val _showThemeSelector = MutableStateFlow(false)
    val showThemeSelector: StateFlow<Boolean> = _showThemeSelector.asStateFlow()

    val _showFontSlider = MutableStateFlow(false)
    val showFontSlider: StateFlow<Boolean> = _showFontSlider.asStateFlow()

    // this part is temporary, it fetches the book from Raw but it should be replaced by fetching the book by id in the init{}
    private val metadata = getMetadata(context)
    val bookInfo = getBookInfo(context)
    val book = fetchBook(bookInfo)
    val offsets = metadata.tokenOffsets
    var tokenName: String = ""

    // clickable bookmark span listener
    val _bookmarkClickEvent = MutableStateFlow<IHBookmarkClickableSpan?>(null)
    val bookmarkClickEvent: StateFlow<IHBookmarkClickableSpan?> = _bookmarkClickEvent.asStateFlow()

    val _editBookmark = MutableStateFlow<Boolean>(false)
    val editBookmark: StateFlow<Boolean> = _editBookmark.asStateFlow()

    val _bookmarkTitle = MutableStateFlow("")
    val bookmarkTitle: StateFlow<String> = _bookmarkTitle

    val _bookmarkEnd = MutableStateFlow(0)
    val bookmarkEnd: StateFlow<Int> = _bookmarkEnd

    val _bookmarkStart = MutableStateFlow(0)
    val bookmarkStart: StateFlow<Int> = _bookmarkStart

    val _bookmarkPageNumber = MutableStateFlow(0)
    val bookmarkPageNumber: StateFlow<Int> = _bookmarkPageNumber

    val _textView = MutableStateFlow<IHTextView?>(null)
    val textView: StateFlow<IHTextView?> = _textView

    val _showTopBar = MutableStateFlow(true)
    val showTopBar: StateFlow<Boolean> = _showTopBar.asStateFlow()

    //val listener = bookmarkListener
    var getAllHighlightsJob: Job? = null
    var getAllBookmarksJob: Job? = null

    private val _fontSize = MutableStateFlow(preferencesManager.getFontSize())
    val fontSize: StateFlow<Float> = _fontSize

    private val _fontColor = MutableStateFlow(preferencesManager.getFontColor())
    val fontColor = _fontColor

    private val _darkTheme = MutableStateFlow(preferencesManager.isDarkTheme())
    val darkTheme = _darkTheme

    private val _pinnedTopBar = MutableStateFlow(preferencesManager.isPinnedTopBar())
    val pinnedTopBar = _pinnedTopBar

    val _pageNumber = mutableStateOf<Int>(1)
    val pageNumber: State<Int> = _pageNumber

    private val _fontWeight = MutableStateFlow(preferencesManager.getFontWeight())
    val fontWeight = _fontWeight

    private val _backgroundColor = MutableStateFlow(preferencesManager.getBackgroundColor())
    val backgroundColor = _backgroundColor

    val _linkNavigationPage = MutableStateFlow<Boolean>(false)
    val linkNavigationPage: StateFlow<Boolean> = _linkNavigationPage

    val bookmarkSpans = mutableMapOf<Long, IHBookmarkClickableSpan>()


    private val _pageHighlights = MutableStateFlow<List<Highlight>>(emptyList())
    val pageHighlights: StateFlow<List<Highlight>> = _pageHighlights

    private val _currentPageIndex = mutableIntStateOf(0)
    val currentPageIndex: State<Int> = _currentPageIndex

    private val _targetIndex = mutableStateOf<Int?>(null)
    val targetIndex: State<Int?> = _targetIndex

    private val _state = mutableStateOf(BookDetailsState())
    val state: State<BookDetailsState> = _state

    init {
        // this part is temporary, it fetches the book from Raw but it should be replaced by fetching the book by id in the init{}
        for (i in offsets) {
            if (i.firstChapterNumber <= 1 && i.lastChapterNumber >= 1) {
                tokenName = i.tokenName
            }
        }
        val chapter5Id = getRawFileByName(context, tokenName)
        val chapter5 = chapter5Id?.let { readFileFromRaw(context, it) }
        if (chapter5 != null) {
            val decodedText = Decryption.decryption(chapter5)
            book.addChapter(decodedText, metadata.encoding)
        }
        viewModelScope.launch {
            _state.value = state.value.copy(
                bookId = book.bookInfo.id,
                bookTitle = book.bookInfo.name,
                bookAuthor = book.bookInfo.author.name,
                // bookCategory = book.category,
                numberOfChapters = book.bookInfo.chaptersNumber,
                bookDescription = book.bookInfo.description,
                // imageUrl = book.bookInfo.cover,
            )
            //getHighlightsForPage(1)
            getBookBookmarks(bookInfo.id)
            // getBookmarksForBook(state.value.bookId)
            Log.d(
                "BookContentViewModel",
                "spannable content = ${state.value.spannableContent.length}"
            )
        }
    }

    fun onImageClick(imageData: ByteArray?) {
        _imageClicked.value = imageData
    }

    fun setPinnedTopBar(isPinned: Boolean) {
        preferencesManager.setPinnedTopBar(isPinned)
        _pinnedTopBar.value = isPinned
    }
    fun setVerticalScroll(isVertical: Boolean) {
        preferencesManager.setVerticalScroll(isVertical)
        _verticalScroll.value = isVertical
    }
    fun setDarkTheme(isDark: Boolean, textColor: Int, backgroundColor: Int) {
        preferencesManager.setDarkTheme(isDark)
        _darkTheme.value = isDark
        saveFontColor(textColor)
        saveBackgroundColor(backgroundColor)
    }

    fun saveFontSize(fontSize: Float) {
        preferencesManager.setFontSize(fontSize)
        _fontSize.value = fontSize
    }

    fun saveFontColor(color: Int) {
        preferencesManager.setFontColor(color)
        _fontColor.value = color
    }

    fun saveFontWeight(fontWeight: Int) {
        preferencesManager.setFontWeight(fontWeight)
        _fontWeight.value = fontWeight
    }

    fun saveBackgroundColor(backgroundColor: Int) {
        preferencesManager.setBackgroundColor(backgroundColor)
        _backgroundColor.value = backgroundColor
    }

    fun addHighlight(pageNumber: Int, start: Int, end: Int, textView: IHTextView) {
        val originalStart = textView.getDatabaseIndex(start)
        val originalEnd = textView.getDatabaseIndex(end)
        viewModelScope.launch {
            Log.d("BookContentViewModel", "addHighlight is invoked from {$start} to {$end}")
            val bookId = _state.value.bookId
            val highlight = Highlight(
                bookId = bookId,
                start = originalStart,
                end = originalEnd,
                chapterNumber = state.value.currentChapterNumber,
                pageNumber = pageNumber
            )
            val highlightId = highlightUseCases.addHighlight(highlight)
            Log.d("BookContentViewModel", "highlight is saved from {$start} to {$end}")
            Log.d("BookContentViewModel", "getHighlightsForBook is invoked")
            textView.drawSingleHighlight(highlightId, start, end, true)
        }

    }

    fun removeHighlightById(highlightId: Long) {
        viewModelScope.launch {
            highlightUseCases.removeHighlightById(highlightId)
        }
    }

    fun removeBookmarkById(bookmarkId: Long) {
        viewModelScope.launch {
            bookmarkUseCases.removeBookmarkById(bookmarkId)
            bookmarkSpans[bookmarkId]?.let { textView.value?.removeSingleCustomSpan(it) }
        }
    }

    //updates bookmark title for existing bookmarks
    fun updateBookmarkTitle(bookmarkId: Long, newTitle: String) {
        viewModelScope.launch {
            bookmarkUseCases.updateBookmarkTitle(bookmarkId, newTitle)
            bookmarkSpans[bookmarkId]?.bookmarkName = newTitle
        }
    }

    fun addBookmark(bookmarkName: String, start: Int, end: Int, textView: IHTextView) {
        val originalStart = textView.getDatabaseIndex(start)
        val originalEnd = textView.getDatabaseIndex(end)
        viewModelScope.launch {
            val bookId = _state.value.bookId
            val newBookmark = Bookmark(
                bookId = bookId,
                start = originalStart,
                end = originalEnd,
                chapterNumber = state.value.currentChapterNumber,
                pageNumber = bookmarkPageNumber.value,
                bookmarkTitle = bookmarkName
            )
            val newBookmarkId = bookmarkUseCases.addBookmark(newBookmark)
//            _state.value= state.value.copy(
//                bookBookmarks = state.value.bookBookmarks + newBookmark
//            )
            getBookBookmarks(bookId)
            for (i in state.value.bookBookmarks) {
                Log.d("new", "bookmark = ${i.bookmarkTitle}")
            }
            Log.d("rasm", "addBookmark is invoked page# = ${textView.pageNumber}")
            textView.drawSingleBookmark(
                newBookmarkId,
                newBookmark.bookmarkTitle,
                start,
                end,
                true
            )
        }
    }

    fun getHighlightsForPage(pageNumber: Int, textView: IHTextView) {
        viewModelScope.launch {
            highlightUseCases.getPageHighlights(
                bookId = state.value.bookId,
                chapterNumber = state.value.currentChapterNumber,
                pageNumber = pageNumber
            ).onEach { highlights ->
                _state.value = state.value.copy(
                    bookHighlights = highlights
                )
                Log.d(
                    "IHTextView",
                    "pageNumber = $pageNumber getHighlightsForBook is successful $highlights"
                )
                _pageHighlights.value = _state.value.bookHighlights
                textView.drawAllHighlights(state.value.bookHighlights)

            }.launchIn(viewModelScope)

            Log.d("IHTextView", "HighlightsForBook is spanned")
        }
    }

    fun getBookmarksForPage(pageNumber: Int, textView: IHTextView) {
        Log.d("IHTextView", "getBookmarksForPage is called")
        val pageBookmarks = state.value.bookBookmarks.filter {
            it.pageNumber == pageNumber
        }
        Log.d("IHTextView", "all bookmarks = ${state.value.bookBookmarks.size}")
        Log.d("IHTextView", "getBookmarksForPage # $pageNumber = ${pageBookmarks.size}")
        pageBookmarks.forEach { bookmark ->
            textView.drawSingleBookmark(
                bookmark.id,
                bookmark.bookmarkTitle,
                bookmark.start,
                bookmark.end,
                false
            )
            Log.d("IHTextView", "page bookmark list = ${bookmark.bookmarkTitle}")
        }
    }

    private fun getBookBookmarks(bookId: String) {
        viewModelScope.launch {
            bookmarkUseCases.getBookmarksForBook(
                bookId,
            ).onEach { bookmarks ->
                _state.value = state.value.copy(
                    bookBookmarks = bookmarks
                )
                Log.d("BookContentViewModel", "getBookmarksForBook is successful $bookmarks")
            }
                .launchIn(viewModelScope)
        }
    }

    private suspend fun getHighlightsForBook(pageNumber: Int) {
        getAllHighlightsJob?.cancel()
        try {
            Log.d("BookContentViewModel", "getHighlightsForBook is called $pageNumber")
            getAllHighlightsJob = highlightUseCases.getPageHighlights(
                bookId = state.value.bookId,
                chapterNumber = state.value.currentChapterNumber,
                pageNumber = pageNumber
            ).onEach { highlights ->
                _state.value = state.value.copy(
                    bookHighlights = highlights
                )

                Log.d("BookContentViewModel", "getHighlightsForBook is successful $highlights")
                _pageHighlights.value = _state.value.bookHighlights
            }.launchIn(viewModelScope)
            Log.d("BookContentViewModel", "HighlightsForBook is spanned")
        } catch (e: Exception) {
            Log.e("BookContentViewModel", "Error getting Book highlights: ${e.message}")
        }
    }

    private suspend fun getBookmarksForBook(bookId: String) {
        getAllBookmarksJob?.cancel()
        try {
            Log.d("BookContentViewModel", "getBookmarksForBook is called")
            getAllBookmarksJob = bookmarkUseCases.getBookmarksForBook(
                bookId,
            ).onEach { bookmarks ->
                _state.value = state.value.copy(
                    bookBookmarks = bookmarks
                )
                Log.d("BookContentViewModel", "getBookmarksForBook is successful $bookmarks")
            }
                .launchIn(viewModelScope)
        } catch (e: Exception) {
            Log.e("BookContentViewModel", "Error getting Book bookmarks: ${e.message}")
        }
    }


    fun scrollToIndexLazy(targetPageIndex: Int, lazyListState: LazyListState, targetIndex: Int) {

        viewModelScope.launch {
            if (targetPageIndex != lazyListState.firstVisibleItemIndex) {
                // Scroll to the target page if it's not already visible
                lazyListState.scrollToItem(targetPageIndex)

                snapshotFlow { lazyListState.isScrollInProgress }
                    .filter { !it } // Wait until scrolling is finished
                    .first() // Take only the first emission
                    .let {
                        _linkNavigationPage.value = true
                        delay(200)
                        if (!linkNavigationPage.value) {
                            val targetLine = textView.value?.getYCoordinateForIndex(targetIndex)
                            lazyListState.scrollBy(targetLine!!.toFloat())
                            _showTopBar.value = false
                        }
                    }
            } else {
                // Target page is already visible, scroll directly to the index
                val targetLine = textView.value?.getYCoordinateForIndex(targetIndex)
                lazyListState.scrollBy(targetLine!!.toFloat())
                _showTopBar.value = false
            }

        }
    }

    fun navigateToPage(targetPageIndex: Int, lazyListState: LazyListState) {
        if (targetPageIndex in 0 until lazyListState.layoutInfo.totalItemsCount){
            viewModelScope.launch {
                lazyListState.scrollToItem(targetPageIndex)
                _showTopBar.value = false
            }
        }
        else {
            _showTopBar.value = false
            showToast(context, "Page not found")
        }

    }
}




