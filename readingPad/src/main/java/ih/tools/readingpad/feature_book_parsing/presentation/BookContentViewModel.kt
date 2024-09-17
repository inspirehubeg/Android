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
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.fetchBook
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getBookInfo
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getMetadata
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getRawFileByName
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.readFileFromRaw
import ih.tools.readingpad.feature_book_parsing.data.PreferencesManager
import ih.tools.readingpad.feature_book_parsing.domain.model.BookDetailsState
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView
import ih.tools.readingpad.feature_bookmark.domain.model.Bookmark
import ih.tools.readingpad.feature_bookmark.domain.use_cases.BookmarkUseCases
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkClickableSpan
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkSpan
import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.use_cases.HighlightUseCases
import ih.tools.readingpad.feature_note.domain.model.Note
import ih.tools.readingpad.feature_note.domain.use_cases.NoteUseCases
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.use_case.ThemeColorUseCases
import ih.tools.readingpad.util.IHNoteClickableSpan
import ih.tools.readingpad.util.IHNoteSpan
import ih.tools.readingpad.util.IHSpan
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
    private val noteUseCases: NoteUseCases,
    private val context: Context
) : ViewModel() {

    private val _savedFontColors = MutableLiveData<List<ThemeColor>>()
    val savedFontColors = _savedFontColors

    private val _savedBackgroundColors = MutableLiveData<List<ThemeColor>>()
    val savedBackgroundColors = _savedBackgroundColors

    private fun getSavedColors() {
        viewModelScope.launch {
            _savedFontColors.value =
                themeColorUseCases.getThemeColorsUseCase(type = ThemeColorType.FONT)

            _savedBackgroundColors.value =
                themeColorUseCases.getThemeColorsUseCase(type = ThemeColorType.BACKGROUND)
        }
    }

//    private val _imageClicked = MutableStateFlow<ByteArray?>(null)
//    val imageClicked: StateFlow<ByteArray?> = _imageClicked.asStateFlow()
//    fun onImageClick(imageData: ByteArray?) {
//        _imageClicked.value = imageData
//    }

//    private val _verticalScroll = MutableStateFlow(preferencesManager.isVerticalScroll())
//    val verticalScroll = _verticalScroll.asStateFlow()
//    fun setVerticalScroll(isVertical: Boolean) {
//        preferencesManager.setVerticalScroll(isVertical)
//        _verticalScroll.value = isVertical
//    }

    private val _oneFingerScroll = MutableStateFlow(true)
    val oneFingerScroll = _oneFingerScroll.asStateFlow()
    fun setOneFingerScroll(value: Boolean) {
        Log.d("test", "setOneFingerScroll is called $value")
        _oneFingerScroll.value = value
    }

//    private val _isDrawerOpen = MutableStateFlow(false)
//    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

//    fun openDrawer() {
//        _isDrawerOpen.value = true
//    }
//
//    fun closeDrawer() {
//        _isDrawerOpen.value = false
//    }

//    private val _drawerGesturesEnabled = MutableStateFlow(true)
//    val drawerGesturesEnabled: StateFlow<Boolean> = _drawerGesturesEnabled.asStateFlow()
//    fun setDrawerGesturesEnabled(value: Boolean) {
//        _drawerGesturesEnabled.value = value
//    }

//    private val _showUserInputPage = MutableStateFlow(false)
//    val showUserInputPage = _showUserInputPage.asStateFlow()
//    fun toggleShowUserInputPage() {
//        _showUserInputPage.value = !_showUserInputPage.value
//    }

//    private val _showCustomThemePage = MutableStateFlow(false)
//    val showCustomThemePage = _showCustomThemePage.asStateFlow()
//    fun setShowCustomThemePage(open: Boolean) {
//        _showCustomThemePage.value = open
//    }

//    private val _showBrightnessDialog = MutableStateFlow(false)
//    val showBrightnessDialog = _showBrightnessDialog.asStateFlow()
//    fun setShowBrightnessDialog(open: Boolean) {
//        _showBrightnessDialog.value = open
//    }

//    private val _showBookmarkListDialog = MutableStateFlow(false)
//    val showBookmarkListDialog = _showBookmarkListDialog.asStateFlow()
//    fun setShowBookmarkListDialog(show: Boolean) {
//        _showBookmarkListDialog.value = show
//    }

//    private val _showPageNumberDialog = MutableStateFlow(false)
//    val showPageNumberDialog = _showPageNumberDialog.asStateFlow()
//    fun setShowPageNumberDialog(show: Boolean) {
//        _showPageNumberDialog.value = show
//    }

//    private val _showPagesSlider = MutableStateFlow(false)
//    val showPagesSlider = _showPagesSlider.asStateFlow()
//    fun setShowPagesSlider(show: Boolean) {
//        _showPagesSlider.value = show
//    }

//    private val _showHighlightsBookmarks = MutableStateFlow(true)
//    val showHighlightsBookmarks = _showHighlightsBookmarks.asStateFlow()
//    fun setShowHighlightsBookmarks(show: Boolean) {
//        _showHighlightsBookmarks.value = show
//    }

//    private val _showAddBookmarkDialog = MutableStateFlow(false)
//    val showAddBookmarkDialog = _showAddBookmarkDialog.asStateFlow()
//    fun setShowAddBookmarkDialog(show: Boolean) {
//        _showAddBookmarkDialog.value = show
//    }

//    private val _showEditBookmarkDialog = MutableStateFlow(false)
//    val showEditBookmarkDialog = _showEditBookmarkDialog.asStateFlow()
//    fun setShowEditBookmarkDialog(show: Boolean) {
//        _showEditBookmarkDialog.value = show
//    }

//    private val _keepScreenOn = MutableStateFlow(false)
//    val keepScreenOn = _keepScreenOn.asStateFlow()
//    fun setKeepScreenOn(keep: Boolean) {
//        _keepScreenOn.value = keep
//        viewModelScope.launch {
//            _keepScreenOnEvent.emit(keep)
//        }
//    }
//    private val _keepScreenOnEvent = MutableSharedFlow<Boolean>()
//    val keepScreenOnEvent = _keepScreenOnEvent.asSharedFlow()


//    private val _showFullScreenImage = MutableStateFlow(false)
//    val showFullScreenImage = _showFullScreenImage.asStateFlow()
//    fun setShowFullScreenImage(open: Boolean) {
//        _showFullScreenImage.value = open
//    }

//    private val _showThemeSelector = MutableStateFlow(false)
//    val showThemeSelector: StateFlow<Boolean> = _showThemeSelector.asStateFlow()
//    fun setShowThemeSelector(show: Boolean) {
//        _showThemeSelector.value = show
//    }

//    private val _showFontSlider = MutableStateFlow(false)
//    val showFontSlider: StateFlow<Boolean> = _showFontSlider.asStateFlow()
//    fun setShowFontSlider(show: Boolean) {
//        _showFontSlider.value = show
//    }

//    private val _showCustomSelectionMenu = MutableStateFlow(false)
//    val showCustomSelectionMenu: StateFlow<Boolean> = _showCustomSelectionMenu.asStateFlow()
//    fun setShowCustomSelectionMenu(show: Boolean) {
//        _showCustomSelectionMenu.value = show
//    }

//    private val _preferredHighlightColor = MutableStateFlow(HighlightLaserLemon)
//    val preferredHighlightColor: StateFlow<Color> = _preferredHighlightColor.asStateFlow()
//    fun setPreferredHighlightColor(color: Color) {
//        _preferredHighlightColor.value = color
//    }

    private val _selectedSpans = MutableStateFlow<List<IHSpan>>(emptyList())
    val selectedSpans: StateFlow<List<IHSpan>> = _selectedSpans.asStateFlow()
    fun setSelectedSpans(spans: List<IHSpan>) {
        _selectedSpans.value = spans
    }


    // clickable bookmark span listener
//    private val _bookmarkClickEvent = MutableStateFlow<IHBookmarkClickableSpan?>(null)
//    val bookmarkClickEvent: StateFlow<IHBookmarkClickableSpan?> = _bookmarkClickEvent.asStateFlow()
//    fun setBookmarkClickEvent(span: IHBookmarkClickableSpan?) {
//        _bookmarkClickEvent.value = span
//    }

    // clickable note span listener
//    private val _noteClickEvent = MutableStateFlow<IHNoteClickableSpan?>(null)
//    val noteClickEvent: StateFlow<IHNoteClickableSpan?> = _noteClickEvent.asStateFlow()
//    fun setNoteClickEvent(span: IHNoteClickableSpan?) {
//        _noteClickEvent.value = span
//    }

//    private val _editNote = MutableStateFlow(false)
//    val editNote: StateFlow<Boolean> = _editNote.asStateFlow()
//    fun setEditNote(edit: Boolean) {
//        _editNote.value = edit
//    }

//    private val _noteEnd = MutableStateFlow(0)
//    val noteEnd: StateFlow<Int> = _noteEnd
//    fun setNoteEnd(end: Int) {
//        _noteEnd.value = end
//    }

//    private val _noteStart = MutableStateFlow(0)
//    val noteStart: StateFlow<Int> = _noteStart
//    fun setNoteStart(start: Int) {
//        _noteStart.value = start
//    }

//    private val _showAddNoteDialog = MutableStateFlow(false)
//    val showAddNoteDialog = _showAddNoteDialog.asStateFlow()
//    fun setShowAddNoteDialog(show: Boolean) {
//        _showAddNoteDialog.value = show
//    }

//    private val _showEditNoteDialog = MutableStateFlow(false)
//    val showEditNoteDialog = _showEditNoteDialog.asStateFlow()
//    fun setShowEditNoteDialog(show: Boolean) {
//        _showEditNoteDialog.value = show
//    }

//    private val _imageRotation = MutableStateFlow(0f)
//    val imageRotation: StateFlow<Float> = _imageRotation.asStateFlow()
//    fun setImageRotation(rotation: Float) {
//        _imageRotation.value = rotation
//    }

//    private val _editBookmark = MutableStateFlow(false)
//    val editBookmark: StateFlow<Boolean> = _editBookmark.asStateFlow()
//    fun setEditBookmark(edit: Boolean) {
//        _editBookmark.value = edit
//    }

//    private val _bookmarkEnd = MutableStateFlow(0)
//    val bookmarkEnd: StateFlow<Int> = _bookmarkEnd
//    fun setBookmarkEnd(end: Int) {
//        _bookmarkEnd.value = end
//    }

//    private val _bookmarkStart = MutableStateFlow(0)
//    val bookmarkStart: StateFlow<Int> = _bookmarkStart
//    fun setBookmarkStart(start: Int) {
//        _bookmarkStart.value = start
//    }


//    private val _bookmarkName = MutableStateFlow("")
//    val bookmarkName: StateFlow<String> = _bookmarkName
//    fun setBookmarkName(name: String) {
//        _bookmarkName.value = name
//    }

//    private val _bookmarkPageNumber = MutableStateFlow(0)
//    private val bookmarkPageNumber: StateFlow<Int> = _bookmarkPageNumber
//    fun setBookmarkPageNumber(pageNumber: Int) {
//        _bookmarkPageNumber.value = pageNumber
//    }

//    private val _notePageNumber = MutableStateFlow(0)
//    private val notePageNumber: StateFlow<Int> = _notePageNumber
//    fun setNotePageNumber(pageNumber: Int) {
//        _notePageNumber.value = pageNumber
//    }

    private val _textView = MutableStateFlow<IHTextView?>(null)
    val textView: StateFlow<IHTextView?> = _textView
    fun setTextView(textView: IHTextView) {
        _textView.value = textView
    }

    private val _showTopBar = MutableStateFlow(true)
    val showTopBar: StateFlow<Boolean> = _showTopBar.asStateFlow()
    fun setTopBarVisibility(isVisible: Boolean) {
        _showTopBar.value = isVisible
    }

    private val _fontColor = MutableStateFlow(preferencesManager.getFontColor())
    val fontColor = _fontColor
    fun setFontColor(color: Int) {
        preferencesManager.setFontColor(color)
        _fontColor.value = color
    }

    private val _darkTheme = MutableStateFlow(preferencesManager.isDarkTheme())
    val darkTheme = _darkTheme
    fun setDarkTheme(isDark: Boolean, textColor: Int, backgroundColor: Int) {
        preferencesManager.setDarkTheme(isDark)
        _darkTheme.value = isDark
        setFontColor(textColor)
        setBackgroundColor(backgroundColor)
    }

//    private val _pinnedTopBar = MutableStateFlow(preferencesManager.isPinnedTopBar())
//    val pinnedTopBar = _pinnedTopBar
//    fun setPinnedTopBar(isPinned: Boolean) {
//        preferencesManager.setPinnedTopBar(isPinned)
//        _pinnedTopBar.value = isPinned
//    }

    private val _pageNumber = mutableIntStateOf(1)
    val pageNumber: State<Int> = _pageNumber
    fun setPageNumber(number: Int) {
        _pageNumber.intValue = number
        getCurrentChapterIndex(number)
    }

    fun getCurrentChapterIndex(pageNumber: Int): Int {
        if (book.chapters.isEmpty()) {
            Log.d("PagesScreen", "book chapters are empty")
            return -1
        } // Handle empty case
        for (i in 0 until book.chapters.size) {
            book.chapters[i].pages.forEach { page ->
                Log.d("PagesScreen", "check page number = ${page.pageNumber}")
                if (page.pageNumber == pageNumber) {
                    Log.d(
                        "PagesScreen",
                        "page number = ${page.pageNumber} current chapter index = $i"
                    )
                    return i
                }
            }
        }
        return -1
    }

    private val _fontWeight = MutableStateFlow(preferencesManager.getFontWeight())
    val fontWeight = _fontWeight
    fun setFontWeight(fontWeight: Int) {
        preferencesManager.setFontWeight(fontWeight)
        _fontWeight.value = fontWeight
    }

    private val _backgroundColor = MutableStateFlow(preferencesManager.getBackgroundColor())
    val backgroundColor = _backgroundColor
    fun setBackgroundColor(backgroundColor: Int) {
        preferencesManager.setBackgroundColor(backgroundColor)
        _backgroundColor.value = backgroundColor
    }

    private val _currentThemeFontColor = MutableLiveData(fontColor.value)
    val currentThemeFontColor = _currentThemeFontColor
    fun setCurrentThemeFontColor(colorInt: Int) {
        _currentThemeFontColor.value = colorInt
    }

    private val _currentThemeBackgroundColor = MutableLiveData(backgroundColor.value)
    val currentThemeBackgroundColor = _currentThemeBackgroundColor
    fun setCurrentThemeBackgroundColor(colorInt: Int) {
        _currentThemeBackgroundColor.value = colorInt
    }

    private val _linkNavigationPage = MutableStateFlow(false)
    val linkNavigationPage: StateFlow<Boolean> = _linkNavigationPage
    fun setLinkNavigationPage(value: Boolean) {
        _linkNavigationPage.value = value
    }

    private val _fontSize = MutableStateFlow(preferencesManager.getFontSize())
    val fontSize: StateFlow<Float> = _fontSize

    private val _fontSizeChanged = MutableStateFlow(false)
    val fontSizeChanged: StateFlow<Boolean> = _fontSizeChanged

    fun setFontSizeChanged(value: Boolean) {
        _fontSizeChanged.value = value
    }

    fun setFontSize(fontSize: Float, currentY: Float) {
        _currentPageIndex.value = lazyListState.firstVisibleItemIndex
        val fontSizePercentage = fontSize / _fontSize.value

        preferencesManager.setFontSize(fontSize)
        _fontSize.value = fontSize
        var y = 0
        y = lazyListState.firstVisibleItemScrollOffset

        val newY = y * fontSizePercentage * 1.01f
        scrollToIndex((newY - y))
        _fontSizeChanged.value = true
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // this part is temporary, it fetches the book from Raw but it should be replaced by
    // fetching the book by id in the init{}
    private val metadata = getMetadata(context)
    private val bookInfo = getBookInfo(context)
    val book = fetchBook(bookInfo)
    private val offsets = metadata.tokenOffsets.sortedBy { it.firstChapterNumber }
    private var tokenName: String = ""

    private var getAllHighlightsJob: Job? = null
    private var getAllBookmarksJob: Job? = null
    val bookmarkClickableSpans = mutableMapOf<Long, IHBookmarkClickableSpan>()
    val bookmarkSpans = mutableMapOf<Long, IHBookmarkSpan>()

    val noteClickableSpans = mutableMapOf<Long, IHNoteClickableSpan>()
    val noteSpans = mutableMapOf<Long, IHNoteSpan>()

    private val _state = mutableStateOf(BookDetailsState())
    val state: State<BookDetailsState> = _state

    var lazyListState = LazyListState()
    private val _currentPageIndex = MutableStateFlow(0)
    val currentPageIndex: StateFlow<Int> = _currentPageIndex

    private val _currentChapterIndex = mutableIntStateOf(0)
    val currentChapterIndex: State<Int> = _currentChapterIndex

    //    private val _tokenIndex = MutableStateFlow(0)
//    val tokenIndex: StateFlow<Int> = _tokenIndex
//    fun setTokenIndex(index: Int) {
//        _tokenIndex.value = index
//    }
    var tokenIndex = 0
    private val _chapterCount = mutableIntStateOf(0)
    val chapterCount: State<Int> = _chapterCount

    fun fetchNextChapter() {
        // size of the lazy list
        // current page index
        // if current page index == size of the lazy list - 2 then fetch the next chapter
        if (tokenIndex < offsets.size) {
            tokenName = offsets[tokenIndex++].tokenName
            Log.d("PagesScreen", " tokenName = $tokenName")
            val chapterId = getRawFileByName(context, tokenName)
            val chapter = chapterId?.let { readFileFromRaw(context, it) }
            if (chapter != null) {
                val decodedText = Decryption.decryption(chapter)
                book.addChapter(decodedText, metadata.encoding)
                _chapterCount.intValue = book.chapters.size // Update chapter count
            }
        }
    }

    init {
        // this part is temporary, it fetches the book from Raw but it should be replaced by fetching the book by id in the init{}
        fetchNextChapter()

        viewModelScope.launch {
            _state.value = state.value.copy(
                bookId = book.bookInfo.id,
                bookTitle = book.bookInfo.name,
                bookAuthor = book.bookInfo.author.name,
                // bookCategory = book.category,
                numberOfChapters = book.bookInfo.chaptersNumber,
                numberOfPages = book.bookInfo.pagesNumber,
                bookDescription = book.bookInfo.description,

                // imageUrl = book.bookInfo.cover,
            )
            getBookBookmarks(bookInfo.id)
            getBookHighlights(bookInfo.id)
            getBookNotes(bookInfo.id)
            Log.d(
                "BookContentViewModel",
                "spannable content = ${state.value.spannableContent.length}"
            )
        }
        getSavedColors()
    }


    fun addHighlight(
        pageNumber: Int,
        start: Int,
        end: Int,
        textView: IHTextView,
        highlightText: String,
        color: Int
    ) {
        val originalStart = textView.getDatabaseIndex(start)
        val originalEnd = textView.getDatabaseIndex(end)
        viewModelScope.launch {
            Log.d("BookContentViewModel", "addHighlight is invoked from {$start} to {$end}")
            val bookId = _state.value.bookId
            val highlight = Highlight(
                bookId = bookId,
                start = originalStart,
                end = originalEnd,
                chapterNumber = textView.chapterNumber,
                pageNumber = pageNumber,
                text = highlightText,
                color = color
            )
            val highlightId = highlightUseCases.addHighlight(highlight)
            Log.d("BookContentViewModel", "highlight is saved from {$start} to {$end}")
            Log.d("BookContentViewModel", "getHighlightsForBook is invoked")
            textView.drawSingleHighlight(highlightId, start, end, true, color = color)
            // _state.value.bookHighlights.add(highlight)
            getBookHighlights(bookId)
        }

    }

    fun addBookmark(
        bookmarkName: String,
        textView: IHTextView,
        start: Int,
        end: Int,
        pageNumber: Int
    ) {
        val originalStart = textView.getDatabaseIndex(start)
        val originalEnd = textView.getDatabaseIndex(end)
        viewModelScope.launch {
            val bookId = _state.value.bookId
            val newBookmark = Bookmark(
                bookId = bookId,
                start = originalStart,
                end = originalEnd,
                chapterNumber = textView.chapterNumber,
                pageNumber = pageNumber,
                bookmarkTitle = bookmarkName
            )
            val newBookmarkId = bookmarkUseCases.addBookmark(newBookmark)
            getBookBookmarks(bookId)

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

    fun addNote(noteText: String, start: Int, end: Int, pageNumber: Int, textView: IHTextView) {
        val originalStart = textView.getDatabaseIndex(start)
        val originalEnd = textView.getDatabaseIndex(end)
        viewModelScope.launch {
            val bookId = _state.value.bookId
            val newNote = Note(
                bookId = bookId,
                start = originalStart,
                end = originalEnd,
                chapterNumber = textView.chapterNumber,
                pageNumber = pageNumber,
                text = noteText
            )
            val newNoteId = noteUseCases.addNote(newNote)
            getBookNotes(bookId)

            Log.d("new", "addBookmark is invoked page# = ${textView.pageNumber}")
            textView.drawSingleNote(
                newNoteId,
                noteText,
                start,
                end,
                true
            )
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
            bookmarkClickableSpans[bookmarkId]?.let {
                textView.value?.removeSingleCustomSpan(it)
            }
            bookmarkSpans[bookmarkId]?.let {
                textView.value?.removeSingleCustomSpan(it)
            }

        }
    }

    fun removeNoteById(noteId: Long) {
        viewModelScope.launch {
            noteUseCases.deleteNoteById(noteId)
            noteClickableSpans[noteId]?.let { textView.value?.removeSingleCustomSpan(it) }
            noteSpans[noteId]?.let { textView.value?.removeSingleCustomSpan(it) }
        }
    }

    //updates bookmark title for existing bookmarks
    fun updateBookmarkTitle(bookmarkId: Long, newTitle: String) {
        viewModelScope.launch {
            bookmarkUseCases.updateBookmarkTitle(bookmarkId, newTitle)
            bookmarkClickableSpans[bookmarkId]?.bookmarkName = newTitle
        }
    }

    fun updateNoteText(noteId: Long, newText: String) {
        viewModelScope.launch {
            noteUseCases.updateNoteText(noteId, newText)
            noteClickableSpans[noteId]?.noteText = newText
        }
    }


    fun getHighlightsForPage(pageNumber: Int, textView: IHTextView) {
        viewModelScope.launch {
            val pageHighlights = state.value.bookHighlights.filter {
                it.chapterNumber == textView.chapterNumber
                        && it.pageNumber == pageNumber
            }
            pageHighlights.apply {
                textView.drawAllHighlights(this)
            }
            Log.d("IHTextView", "HighlightsForBook is spanned")
        }
    }

    fun getBookmarksForPage(pageNumber: Int, textView: IHTextView) {
        viewModelScope.launch {
            val pageBookmarks = state.value.bookBookmarks.filter {
                it.chapterNumber == textView.chapterNumber
                        && it.pageNumber == pageNumber
            }
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
    }

    fun getNotesForPage(pageNumber: Int, textView: IHTextView) {
        viewModelScope.launch {
            val pageNotes = state.value.bookNotes.filter {
                it.chapterNumber == textView.chapterNumber
                        && it.pageNumber == pageNumber
            }
            pageNotes.forEach { note ->
                textView.drawSingleNote(
                    note.id,
                    note.text,
                    note.start,
                    note.end,
                    false
                )
                Log.d("IHTextView", "page notes list = ${note.text}")
            }
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

    private fun getBookHighlights(bookId: String) {
        viewModelScope.launch {
            highlightUseCases.getBookHighlights(
                bookId,
            ).onEach { highlights ->
                _state.value = state.value.copy(
                    bookHighlights = highlights
                )
                Log.d("BookContentViewModel", "getHighlightsForBook is successful $highlights")
            }
                .launchIn(viewModelScope)
        }
    }

    private fun getBookNotes(bookId: String) {
        viewModelScope.launch {
            noteUseCases.getBookNotes(
                bookId,
            ).onEach { notes ->
                _state.value = state.value.copy(
                    bookNotes = notes
                )
                Log.d("BookContentViewModel", "getNotesForBook is successful $notes")
            }
                .launchIn(viewModelScope)
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


//    private suspend fun getHighlightsForBook(pageNumber: Int) {
//        getAllHighlightsJob?.cancel()
//        try {
//            Log.d("BookContentViewModel", "getHighlightsForBook is called $pageNumber")
//            getAllHighlightsJob = highlightUseCases.getPageHighlights(
//                bookId = state.value.bookId,
//                chapterNumber = currentChapterIndex.value + 1,
//                pageNumber = pageNumber
//            ).onEach { highlights ->
//                _state.value = state.value.copy(
//                    bookHighlights = highlights.toMutableList()
//                )
//
//                Log.d("BookContentViewModel", "getHighlightsForBook is successful $highlights")
//            }.launchIn(viewModelScope)
//            Log.d("BookContentViewModel", "HighlightsForBook is spanned")
//        } catch (e: Exception) {
//            Log.e("BookContentViewModel", "Error getting Book highlights: ${e.message}")
//        }
//    }
//
//    private suspend fun getBookmarksForBook(bookId: String) {
//        getAllBookmarksJob?.cancel()
//        try {
//            Log.d("BookContentViewModel", "getBookmarksForBook is called")
//            getAllBookmarksJob = bookmarkUseCases.getBookmarksForBook(
//                bookId,
//            ).onEach { bookmarks ->
//                _state.value = state.value.copy(
//                    bookBookmarks = bookmarks
//                )
//                Log.d("BookContentViewModel", "getBookmarksForBook is successful $bookmarks")
//            }
//                .launchIn(viewModelScope)
//        } catch (e: Exception) {
//            Log.e("BookContentViewModel", "Error getting Book bookmarks: ${e.message}")
//        }
//    }

    private fun scrollToIndex(targetPageIndex: Float) {
        viewModelScope.launch {
            lazyListState.scrollBy(targetPageIndex)
        }
    }

    fun scrollToIndexLazy(targetPageIndex: Int, lazyListState: LazyListState, targetIndex: Int) {
        viewModelScope.launch {
            if (targetPageIndex != lazyListState.firstVisibleItemIndex) {
                _linkNavigationPage.value = true
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
                //_showTopBar.value = false
            }
        } else {
//            val targetPageNumber = targetPageIndex + 1
//           // var targetChapterIndex = 0
//
//            for (i in 0 until book.chapters.size) {
//                 book.chapters[i].pages.forEach { page ->
//                    if (page.pageNumber == targetPageNumber) {
//                        tokenIndex = i
//                        return
//                    }
//            }
//            }
//            fetchNextChapter()

            showToast(context, context.getString(R.string.page_not_found))
        }

    }


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
//    suspend fun getSpannedPagesForChapter(chapterIndex: Int, pages: List<Page>): List<SpannedPage> {
//        _isLoading.value = true
//        val result = convertedPagesCache.getOrPut(chapterIndex) {
//            convertAndCachePages(chapterIndex, pages)
//        }
//        _isLoading.value = false
//        return result
//    }

    /**
     * Converts the pages of a chapter and caches them.
     * Also preloads and caches the pages of the next chapter if available.
     *
     * @param chapterIndex The index of the chapter.
     * @param pages The list of pages for the chapter.
     * @return The list of SpannedPage for the chapter.
     */
//    private suspend fun convertAndCachePages(
//        chapterIndex: Int,
//        pages: List<Page>
//    ): List<SpannedPage> {
//        val spannedPages =
//            convertPagesToSpannedPagesLazy(pages, metadata, context, book, lazyListState, this)
//        convertedPagesCache[chapterIndex] = spannedPages
//
//        // Preload the next chapter if available
//        val nextChapterIndex = chapterIndex + 1
//        if (nextChapterIndex < book.chapters.size) {
//            val nextChapterPages = book.chapters[nextChapterIndex].pages
//            convertedPagesCache[nextChapterIndex] =
//                convertPagesToSpannedPagesLazy(
//                    nextChapterPages,
//                    metadata,
//                    context,
//                    book,
//                    lazyListState,
//                    this,
//                )
//        }
//
//        return spannedPages
//    }
    // handle edge cases like the last chapter not having a next chapter.
    // You can further optimize this by adding a mechanism to remove cached chapters
    // that are far away from the currently viewed chapter if memory usage becomes a concern

}




