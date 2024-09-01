package ih.tools.readingpad.feature_book_parsing.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import book_generation.feature.book_creation.security.Decryption
import dagger.hilt.android.lifecycle.HiltViewModel
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Page
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.fetchBook
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getBookInfo
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getMetadata
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getRawFileByName
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.readFileFromRaw
import ih.tools.readingpad.feature_book_parsing.data.PreferencesManager
import ih.tools.readingpad.feature_book_parsing.domain.model.BookDetailsState
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.convertPagesToSpannedPagesLazy
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.use_cases.BookmarkUseCases
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkClickableSpan
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.use_cases.HighlightUseCases
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.use_case.ThemeColorUseCases
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
    private val themeColorUseCases: ThemeColorUseCases,
    private val context: Context
) : ViewModel() {

    private val _savedFontColors = MutableLiveData<List<ThemeColor>>()
    val savedFontColors = _savedFontColors

    private val _savedBackgroundColors = MutableLiveData<List<ThemeColor>>()
    val savedBackgroundColors = _savedBackgroundColors

    private val _imageClicked = MutableStateFlow<ByteArray?>(null)
    val imageClicked: StateFlow<ByteArray?> = _imageClicked.asStateFlow()

    private val _verticalScroll = MutableStateFlow(preferencesManager.isVerticalScroll())
    val verticalScroll = _verticalScroll.asStateFlow()

    private val _oneFingerScroll = MutableStateFlow(true)
    val oneFingerScroll = _oneFingerScroll.asStateFlow()
    fun setOneFingerScroll(value: Boolean){
        Log.d("test", "setOneFingerScroll is called $value")
        _oneFingerScroll.value = value
    }

    private val _showCustomThemePage = MutableStateFlow(false)
    val showCustomThemePage = _showCustomThemePage.asStateFlow()
    fun setShowCustomThemePage(open: Boolean) {
        _showCustomThemePage.value = open
    }

    private val _showBookmarkListDialog = MutableStateFlow(false)
    val showBookmarkListDialog = _showBookmarkListDialog.asStateFlow()

    private val _showPageNumberDialog = MutableStateFlow(false)
    val showPageNumberDialog = _showPageNumberDialog.asStateFlow()

    private val _showAddBookmarkDialog = MutableStateFlow(false)
    val showAddBookmarkDialog = _showAddBookmarkDialog.asStateFlow()


    private val _showEditBookmarkDialog = MutableStateFlow(false)
    val showEditBookmarkDialog = _showEditBookmarkDialog.asStateFlow()

    private val _showFullScreenImage = MutableStateFlow(false)
    val showFullScreenImage = _showFullScreenImage.asStateFlow()
    fun setShowFullScreenImage(open: Boolean) {
        _showFullScreenImage.value = open
    }

    private val _showThemeSelector = MutableStateFlow(false)
    val showThemeSelector: StateFlow<Boolean> = _showThemeSelector.asStateFlow()

    private val _showFontSlider = MutableStateFlow(false)
    val showFontSlider: StateFlow<Boolean> = _showFontSlider.asStateFlow()

    // this part is temporary, it fetches the book from Raw but it should be replaced by fetching the book by id in the init{}
    private val metadata = getMetadata(context)
    private val bookInfo = getBookInfo(context)
    val book = fetchBook(bookInfo)
    private val offsets = metadata.tokenOffsets
    private var tokenName: String = ""

    // clickable bookmark span listener
    private val _bookmarkClickEvent = MutableStateFlow<IHBookmarkClickableSpan?>(null)
    val bookmarkClickEvent: StateFlow<IHBookmarkClickableSpan?> = _bookmarkClickEvent.asStateFlow()

    private val _imageRotation = MutableStateFlow(0f)
    val imageRotation: StateFlow<Float> = _imageRotation.asStateFlow()
    fun setImageRotation(rotation: Float) {
        _imageRotation.value = rotation
    }

    private val _editBookmark = MutableStateFlow(false)
    val editBookmark: StateFlow<Boolean> = _editBookmark.asStateFlow()

    private val _bookmarkEnd = MutableStateFlow(0)
    val bookmarkEnd: StateFlow<Int> = _bookmarkEnd

    private val _bookmarkStart = MutableStateFlow(0)
    val bookmarkStart: StateFlow<Int> = _bookmarkStart

    private val _bookmarkPageNumber = MutableStateFlow(0)
    private val bookmarkPageNumber: StateFlow<Int> = _bookmarkPageNumber

    private val _textView = MutableStateFlow<IHTextView?>(null)
    val textView: StateFlow<IHTextView?> = _textView

    private val _showTopBar = MutableStateFlow(true)
    val showTopBar: StateFlow<Boolean> = _showTopBar.asStateFlow()

    private var getAllHighlightsJob: Job? = null
    private var getAllBookmarksJob: Job? = null

    private val _fontSize = MutableStateFlow(preferencesManager.getFontSize())
    val fontSize: StateFlow<Float> = _fontSize

    private val _fontColor = MutableStateFlow(preferencesManager.getFontColor())
    val fontColor = _fontColor

    private val _darkTheme = MutableStateFlow(preferencesManager.isDarkTheme())
    val darkTheme = _darkTheme

    private val _pinnedTopBar = MutableStateFlow(preferencesManager.isPinnedTopBar())
    val pinnedTopBar = _pinnedTopBar

    private val _pageNumber = mutableIntStateOf(1)
    val pageNumber: State<Int> = _pageNumber

    private val _fontWeight = MutableStateFlow(preferencesManager.getFontWeight())
    val fontWeight = _fontWeight

    private val _backgroundColor = MutableStateFlow(preferencesManager.getBackgroundColor())
    val backgroundColor = _backgroundColor

    private val _currentThemeFontColor = MutableLiveData(fontColor.value)
    val currentThemeFontColor = _currentThemeFontColor

    private val _currentThemeBackgroundColor = MutableLiveData(backgroundColor.value)
    val currentThemeBackgroundColor = _currentThemeBackgroundColor

    private val _linkNavigationPage = MutableStateFlow(false)
    val linkNavigationPage: StateFlow<Boolean> = _linkNavigationPage

    val bookmarkSpans = mutableMapOf<Long, IHBookmarkClickableSpan>()

    private val _state = mutableStateOf(BookDetailsState())
    val state: State<BookDetailsState> = _state

    var lazyListState = LazyListState()
    private val _currentPageIndex = MutableStateFlow(0)
    val currentPageIndex: StateFlow<Int> = _currentPageIndex

    private val _fontSizeChanged = MutableStateFlow(false)
    val fontSizeChanged: StateFlow<Boolean> = _fontSizeChanged

    fun setFontSizeChanged(value: Boolean) {
        _fontSizeChanged.value = value
    }

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
        getSavedColors()
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
            Log.d("new", "addBookmark is invoked page# = ${textView.pageNumber}")
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

    private fun getSavedColors() {
        viewModelScope.launch {
            _savedFontColors.value =
                themeColorUseCases.getThemeColorsUseCase(type = ThemeColorType.FONT)

            _savedBackgroundColors.value =
                themeColorUseCases.getThemeColorsUseCase(type = ThemeColorType.BACKGROUND)
        }
    }

    // Function to add a color
    fun addThemeColor(argb: Int, type: ThemeColorType) {
        viewModelScope.launch {
            // Check if the color already exists in the database
            val colorCount = themeColorUseCases.colorExistsUseCase(argb)
            if (colorCount == 0) {
                // If the color doesn't exist, insert it
                themeColorUseCases.addThemeColorUseCase(argb, type)
                getSavedColors() // Refresh the colors
            }
        }
    }

    // Functions to delete all colors
    fun deleteAllThemeFontColors() {
        viewModelScope.launch {
            themeColorUseCases.deleteAllThemeColorsUseCase(ThemeColorType.FONT)
            getSavedColors() // Refresh the colors
        }
    }

    fun deleteAllThemeBackgroundColors() {
        viewModelScope.launch {
            themeColorUseCases.deleteAllThemeColorsUseCase(ThemeColorType.BACKGROUND)
            getSavedColors() // Refresh the colors
        }
    }


    // Function to delete a specific color
    fun deleteThemeColor(themeColor: ThemeColor) {
        viewModelScope.launch {
            themeColorUseCases.deleteThemeColorUseCase(themeColor)
            getSavedColors() // Refresh the colors
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

    private fun scrollToIndex(targetPageIndex: Float) {
        viewModelScope.launch {
            lazyListState.scrollBy(targetPageIndex)
        }
    }

    fun scrollToIndexLazy(targetPageIndex: Int, lazyListState: LazyListState, targetIndex: Int) {

        viewModelScope.launch {

            if (targetPageIndex != lazyListState.firstVisibleItemIndex) {
                _linkNavigationPage.value = true
                Log.d(
                    "BookContentViewModel",
                    "linkNavigationPage = ${linkNavigationPage.value}"
                )
                Log.d(
                    "BookContentViewModel",
                    "is current page = ${textView.value?.pageNumber}"
                )

            }
            // Scroll to the target page if it's not already visible
            lazyListState.scrollToItem(targetPageIndex)

            snapshotFlow { lazyListState.isScrollInProgress }
                .filter { !it } // Wait until scrolling is finished
                .first() // Take only the first emission
                .let {
                    delay(200)
                    if (!linkNavigationPage.value) {
                        val targetLine = textView.value?.getYCoordinateForIndex(targetIndex)
                        Log.d("BookContentViewModel", "not current page & targetLine = $targetLine")
                        lazyListState.scrollBy(targetLine!!.toFloat())
                        _showTopBar.value = false
                    }
                }
            // }
//            else {
//                // Target page is already visible, scroll directly to the index
//                val targetLine = textView.value?.getYCoordinateForIndex(targetIndex)
//                Log.d("BookContentViewModel", "is current page & targetLine = $targetLine")
//                lazyListState.scrollBy(-100f)
//                _showTopBar.value = false
//            }

        }
    }

    fun navigateToPage(targetPageIndex: Int, lazyListState: LazyListState) {
        if (targetPageIndex in 0 until lazyListState.layoutInfo.totalItemsCount) {
            viewModelScope.launch {
                lazyListState.scrollToItem(targetPageIndex)
                _showTopBar.value = false
            }
        } else {
            _showTopBar.value = false
            showToast(context, context.getString(R.string.page_not_found))
        }

    }


    //setters functions

    fun setCurrentThemeFontColor(colorInt: Int) {
        _currentThemeFontColor.value = colorInt
    }

    fun setCurrentThemeBackgroundColor(colorInt: Int) {
        _currentThemeBackgroundColor.value = colorInt
    }

    fun setShowBookmarkListDialog(show: Boolean) {
        _showBookmarkListDialog.value = show
    }

    fun setShowPageNumberDialog(show: Boolean) {
        _showPageNumberDialog.value = show
    }

    fun setShowAddBookmarkDialog(show: Boolean) {
        _showAddBookmarkDialog.value = show
    }

    fun setShowEditBookmarkDialog(show: Boolean) {
        _showEditBookmarkDialog.value = show
    }

    fun setShowThemeSelector(show: Boolean) {
        _showThemeSelector.value = show
    }

    fun setShowFontSlider(show: Boolean) {
        _showFontSlider.value = show
    }

    fun setBookmarkClickEvent(span: IHBookmarkClickableSpan?) {
        _bookmarkClickEvent.value = span
    }

    fun setEditBookmark(edit: Boolean) {
        _editBookmark.value = edit
    }

    fun setBookmarkEnd(end: Int) {
        _bookmarkEnd.value = end
    }

    fun setBookmarkStart(start: Int) {
        _bookmarkStart.value = start
    }

    fun setBookmarkPageNumber(pageNumber: Int) {
        _bookmarkPageNumber.value = pageNumber
    }

    fun setTextView(textView: IHTextView) {
        _textView.value = textView
    }

    fun setTopBarVisibility(isVisible: Boolean) {
        _showTopBar.value = isVisible
    }

    fun setPageNumber(number: Int) {
        _pageNumber.intValue = number
    }

    fun setLinkNavigationPage(value: Boolean) {
        _linkNavigationPage.value = value
    }


    fun setFontSize(fontSize: Float, currentY: Float) {
        _currentPageIndex.value = lazyListState.firstVisibleItemIndex
        val fontSizePercentage = fontSize/ _fontSize.value

        Log.d("IHTextView", "lazylist y ${lazyListState.firstVisibleItemScrollOffset}")

        Log.d("IHTextView", "currentY $currentY")

        Log.d("IHTextView", "ratio $fontSizePercentage")
        preferencesManager.setFontSize(fontSize)
        _fontSize.value = fontSize
        var y = 0
//        if (currentY == 0f){
//            //the offset on the top of the screen
//
//        }
        y = lazyListState.firstVisibleItemScrollOffset

        val newY = y*fontSizePercentage *1.01f
        Log.d("john", "y = ${y}")
        Log.d("john", "newY = ${newY}")
        Log.d("john", "move = ${newY-y}")
       scrollToIndex((newY-y))
        _fontSizeChanged.value = true
    }

    fun setFontColor(color: Int) {
        preferencesManager.setFontColor(color)
        _fontColor.value = color
    }

    fun setFontWeight(fontWeight: Int) {
        preferencesManager.setFontWeight(fontWeight)
        _fontWeight.value = fontWeight
    }

    fun setBackgroundColor(backgroundColor: Int) {
        preferencesManager.setBackgroundColor(backgroundColor)
        _backgroundColor.value = backgroundColor
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
        setFontColor(textColor)
        setBackgroundColor(backgroundColor)
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Cache to store converted pages for each chapter.
     * Key: Chapter index
     * Value: List of SpannedPage for the chapter
     */
    private val convertedPagesCache = mutableMapOf<Int, List<SpannedPage>>()

    /**
     * Retrieves the spanned pages for a chapter.
     * If the pages are not cached, they are converted and stored in the cache.
     * The next chapter's pages are also preloaded and cached.
     *
     * @param chapterIndex The index of the chapter.
     * @param pages The list of pages for the chapter.
     * @return The list of SpannedPage for the chapter.
     */
    suspend fun getSpannedPagesForChapter(chapterIndex: Int, pages: List<Page>): List<SpannedPage> {
        _isLoading.value = true
        val result = convertedPagesCache.getOrPut(chapterIndex) {
            convertAndCachePages(chapterIndex, pages)
        }
        _isLoading.value = false
        return result
    }

    /**
     * Converts the pages of a chapter and caches them.
     * Also preloads and caches the pages of the next chapter if available.
     *
     * @param chapterIndex The index of the chapter.
     * @param pages The list of pages for the chapter.
     * @return The list of SpannedPage for the chapter.
     */
    private suspend fun convertAndCachePages(
        chapterIndex: Int,
        pages: List<Page>
    ): List<SpannedPage> {
        val spannedPages =
            convertPagesToSpannedPagesLazy(pages, metadata, context, book, lazyListState, this)
        convertedPagesCache[chapterIndex] = spannedPages

        // Preload the next chapter if available
        val nextChapterIndex = chapterIndex + 1
        if (nextChapterIndex < book.chapters.size) {
            val nextChapterPages = book.chapters[nextChapterIndex].pages
            convertedPagesCache[nextChapterIndex] =
                convertPagesToSpannedPagesLazy(
                    nextChapterPages,
                    metadata,
                    context,
                    book,
                    lazyListState,
                    this
                )
        }

        return spannedPages
    }
    // handle edge cases like the last chapter not having a next chapter.
    // You can further optimize this by adding a mechanism to remove cached chapters
    // that are far away from the currently viewed chapter if memory usage becomes a concern

}




