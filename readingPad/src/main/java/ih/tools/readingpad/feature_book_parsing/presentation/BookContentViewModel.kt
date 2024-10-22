package ih.tools.readingpad.feature_book_parsing.presentation

import alexSchool.network.domain.Book
import alexSchool.network.domain.DetailedBookInfo
import alexSchool.network.domain.Metadata
import alexSchool.network.domain.Token
import android.content.Context
import android.util.Log
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Decryption
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.fetchBook
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getBookInfo
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getMetadata
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getRawFileByName
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.readFileFromRaw
import ih.tools.readingpad.feature_book_parsing.data.PreferencesManager
import ih.tools.readingpad.feature_book_parsing.domain.model.BookDetailsState
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView
import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkEntity
import ih.tools.readingpad.feature_bookmark.domain.use_cases.BookmarkUseCases
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkClickableSpan
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkSpan
import ih.tools.readingpad.feature_highlight.data.data_source.HighlightEntity
import ih.tools.readingpad.feature_highlight.domain.use_cases.HighlightUseCases
import ih.tools.readingpad.feature_note.data.data_source.NoteEntity
import ih.tools.readingpad.feature_note.domain.use_cases.NoteUseCases
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.feature_theme_color.domain.use_case.ThemeColorUseCases
import ih.tools.readingpad.remote.BookContentRepository
import ih.tools.readingpad.util.IHNoteClickableSpan
import ih.tools.readingpad.util.IHNoteSpan
import ih.tools.readingpad.util.IHSpan
import ih.tools.readingpad.util.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
    private val bookContentRepository: BookContentRepository,
    private val savedStateHandle: SavedStateHandle,
    private val context: Context
) : ViewModel() {

//    private val _bookTokens = MutableStateFlow<List<Token>>(emptyList())
//    val bookTokens = _bookTokens.asStateFlow()
    private val _savedFontColors = MutableLiveData<List<ThemeColor>>()
    val savedFontColors = _savedFontColors

    private val _savedBackgroundColors = MutableLiveData<List<ThemeColor>>()
    val savedBackgroundColors = _savedBackgroundColors


    private val _oneFingerScroll = MutableStateFlow(true)
    val oneFingerScroll = _oneFingerScroll.asStateFlow()
    fun setOneFingerScroll(value: Boolean) {
        Log.d("test", "setOneFingerScroll is called $value")
        _oneFingerScroll.value = value
    }


    private val _selectedSpans = MutableStateFlow<List<IHSpan>>(emptyList())
    val selectedSpans: StateFlow<List<IHSpan>> = _selectedSpans.asStateFlow()
    fun setSelectedSpans(spans: List<IHSpan>) {
        _selectedSpans.value = spans
    }


    private val _textView = MutableStateFlow<IHTextView?>(null)
    val textView: StateFlow<IHTextView?> = _textView
    fun setTextView(textView: IHTextView) {
        _textView.value = textView
    }

    private var _newMetadata = MutableStateFlow<Metadata?>(null)
    val newMetadata: StateFlow<Metadata?> = _newMetadata

    private var _savedBookInfo = MutableStateFlow<DetailedBookInfo?>(null)
    val savedBookInfo: StateFlow<DetailedBookInfo?> = _savedBookInfo

    private val _pageNumber = mutableIntStateOf(1)
    val pageNumber: State<Int> = _pageNumber
    fun setPageNumber(number: Int) {
        _pageNumber.intValue = number
        getCurrentChapterIndex(number)
    }

    private val _bookId = mutableIntStateOf(-1)
    val bookId: State<Int> = _bookId


    private val _newBook = MutableStateFlow<Book>(
        Book(
            id = -1,
            name = "",
            pagesNumber = 0,
            chaptersNumber = 0,
            index = "",
            encoding = "",
            bookSize = 0.0f,
            targetLinks = "",
            readingProgress = 0,
            cover = null,
            authorName = "",
            description = ""
        )
    )
    val newBook: StateFlow<Book> = _newBook

    private fun loadBookData(bookId: Int) {
        Log.d("PagesScreen", "loadBookData is called book id = $bookId")
        if (bookId != -1) {
            // Update the ViewModel's state with the saved book details
            viewModelScope.launch {
                fetchBookInfo(bookId)
                fetchMetadata(bookId)
                newFetchFirstChapter(bookId)
                _state.value = state.value.copy(
                    bookId = newBook.value.id,
                    bookTitle = newBook.value.name,
                    bookAuthor = newBook.value.authorName,
                    numberOfChapters = newBook.value.chaptersNumber,
                    numberOfPages = newBook.value.pagesNumber,
                    bookDescription = newBook.value.description,
                    imageUrl = newBook.value.cover.toString(),
                )
                getBookBookmarks(bookId)
                getBookHighlights(bookId)
                getBookNotes(bookId)

            }
        } else {
            // this part is temporary, it fetches the book from Raw but it should be replaced by fetching the book by id in the init{}
            fetchNextChapter()
            viewModelScope.launch {
                _state.value = state.value.copy(
                    bookId = book.oldBookInfo.id,
                    bookTitle = book.oldBookInfo.name,
                    bookAuthor = book.oldBookInfo.oldAuthor.name,
                    // bookCategory = book.category,
                    numberOfChapters = book.oldBookInfo.chaptersNumber,
                    numberOfPages = book.oldBookInfo.pagesNumber,
                    bookDescription = book.oldBookInfo.description,
                    // imageUrl = book.bookInfo.cover,
                )
                getBookBookmarks(bookId)
                getBookHighlights(bookId)
                getBookNotes(bookId)
            }
        }
    }

    private suspend fun fetchBookInfo(bookId: Int) {

        bookContentRepository.getBookInfo(bookId)
            .onEach { Log.d("PagesScreen", "BookInfo emitted: $it") } // Log emitted values
            .catch { e ->
                Log.e("BookContentViewModel", "Error fetching book info: ${e.message}")
            }
            .collect { NbookInfo ->
                Log.d("PagesScreen", "bookInfo = ${NbookInfo}")
                _savedBookInfo.value = NbookInfo

                _newBook.value = Book(
                    id = savedBookInfo.value?.id ?: -1,
                    name = savedBookInfo.value?.name ?: "",
                    pagesNumber = savedBookInfo.value?.pagesNumber ?: 0,
                    chaptersNumber = savedBookInfo.value?.chaptersNumber ?: 0,
                    index = "",
                    encoding = "",
                    bookSize = savedBookInfo.value?.bookSize,
                    targetLinks = "",
                    readingProgress = savedBookInfo.value?.readingProgress,
                    cover = savedBookInfo.value?.cover,
                    authorName = savedBookInfo.value?.authorsName!!.joinToString(", "),
                    description = savedBookInfo.value?.description!!
                )
            }

    }

    private suspend fun fetchMetadata(bookId: Int) {
        bookContentRepository.getMetadata(bookId)
            .onEach { Log.d("PagesScreen", "Metadata emitted: $it") } // Log emitted values
            .catch { e ->
                Log.e("BookContentViewModel", "Error fetching metadata: ${e.message}")
            }
            .collect { Nmetadata ->
                Log.d("PagesScreen", "metadata = ${Nmetadata}")
                _newMetadata.value = Nmetadata
            }
    }


    fun getCurrentChapterIndex(pageNumber: Int): Int {
        if (book.oldChapters.isEmpty()) {
            //Log.d("PagesScreen", "book chapters are empty")
            return -1
        } // Handle empty case
        for (i in 0 until book.oldChapters.size) {
            book.oldChapters[i].oldPages.forEach { page ->
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


    private val _linkNavigationPage = MutableStateFlow(false)
    val linkNavigationPage: StateFlow<Boolean> = _linkNavigationPage
    fun setLinkNavigationPage(value: Boolean) {
        _linkNavigationPage.value = value
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


    private var tokenIndex = 0
    private val _chapterCount = mutableIntStateOf(0)
    val chapterCount: State<Int> = _chapterCount
    private val _count = mutableIntStateOf(0)
    val count: State<Int> = _count


    private fun fetchNextChapter() {
        // size of the lazy list
        // current page index
        // if current page index == size of the lazy list - 2 then fetch the next chapter
        if (tokenIndex < offsets.size) {
            tokenName = offsets[tokenIndex++].tokenName
            Log.d("PagesScreen", " tokenName = $tokenName")
            val chapterId = getRawFileByName(context, tokenName)
            val chapter = chapterId?.let { readFileFromRaw(context, it) }
            if (chapter != null) {
                Log.d("PagesScreen", " chapter is not null ${chapter.length}")
                val decodedText = Decryption.decryption(chapter)
                book.addChapter(decodedText, metadata.oldEncoding)
                //newMetadata.value?.encoding?.let { book.addChapter(decodedText, it) }
                _count.intValue = book.oldChapters.size
                _chapterCount.intValue = book.oldChapters.size // Update chapter count
            } else {
                Log.d("PagesScreen", "chapter is null")
            }
        }
    }

    private suspend fun newFetchFirstChapter(bookId: Int) {
        if (newMetadata.value == null) {
            Log.d("PagesScreen", "metadata is null")
            return
        }
        Log.d("PagesScreen", "chapters number = ${newMetadata.value?.index?.size}")
        if (tokenIndex + 1 <= (newMetadata.value?.index?.size ?: -1)) {
            //token id starts with 0 so we need to fetch the first one here with id 0
            fetchToken(
                bookId = bookId,
                tokenNum = tokenIndex + 1,
                onCollect = { token ->
                    Log.d("PagesScreen", "token = $token")
                    if (token != null) {
                        Log.d("PagesScreen", "chapter is not null")
                        val decodedText = Decryption.decryption(token.content, bookId)
                        Log.d("PagesScreen", "decodedText = $decodedText")
                        newMetadata.value?.let {
                            Log.d("PagesScreen", "metadata is not null")
                            newBook.value.addChapter(
                                tokenBody = decodedText,
                                encoding = it.encoding,
                                chapterNumber = tokenIndex + 1,
                                bookIndex = it.index
                            )
                        }
                        _chapterCount.intValue = newBook.value.chapters.size
                        _count.intValue = newBook.value.chapters.size
                    } else {
                        Log.d("PagesScreen", "chapter is null")
                    }
                })

        } else {
            Log.d("PagesScreen", "token is null")
        }
    }

    init {
        _bookId.intValue = savedStateHandle.get<Int>("bookId") ?: error("Book ID not found")
        getSavedColors()
        Log.d("PagesScreen", "BookContentViewModel init is called newBookId = ${bookId}")
        loadBookData(bookId.value)
    }

    private suspend fun fetchToken(bookId: Int, tokenNum: Int, onCollect: (token: Token) -> Unit) {
        Log.d("PagesScreen", "fetchTokens is called")
        bookContentRepository.getTokens(bookId = bookId, tokenNum = tokenNum).catch { e ->
            Log.e("PagesScreen", "Error fetching tokens: ${e.message}")
        }.collect { token ->
            onCollect(token)
        }
        Log.d("PagesScreen", "fetching token BEFORE RETURN")
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
            val highlightEntity = HighlightEntity(
                bookId = bookId,
                start = originalStart,
                end = originalEnd,
                chapterNumber = textView.chapterNumber,
                pageNumber = pageNumber,
                text = highlightText,
                color = color
            )
            val highlightId = highlightUseCases.addHighlight(highlightEntity)
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
            val newBookmarkEntity = BookmarkEntity(
                bookId = bookId,
                start = originalStart,
                end = originalEnd,
                chapterNumber = textView.chapterNumber,
                pageNumber = pageNumber,
                bookmarkTitle = bookmarkName
            )
            val newBookmarkId = bookmarkUseCases.addBookmark(newBookmarkEntity)
            getBookBookmarks(bookId)

            Log.d("new", "addBookmark is invoked page# = ${textView.pageNumber}")
            textView.drawSingleBookmark(
                newBookmarkId,
                newBookmarkEntity.bookmarkTitle,
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
            val newNoteEntity = NoteEntity(
                bookId = bookId,
                start = originalStart,
                end = originalEnd,
                chapterNumber = textView.chapterNumber,
                pageNumber = pageNumber,
                text = noteText
            )
            val newNoteId = noteUseCases.addNote(newNoteEntity)
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
            val pageHighlights = state.value.bookHighlightEntities.filter {
                it.chapterNumber == textView.chapterNumber
                        && it.pageNumber == pageNumber
            }
            pageHighlights.apply {
                textView.drawAllHighlights(this)
            }
            Log.d("IHTextView", "HighlightsForBook is spanned")
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

    fun getBookmarksForPage(pageNumber: Int, textView: IHTextView) {
        viewModelScope.launch {
            val pageBookmarks = state.value.bookBookmarkEntities.filter {
                it.chapterNumber == textView.chapterNumber
                        && it.pageNumber == pageNumber
            }
            pageBookmarks.forEach { bookmark ->
                textView.drawSingleBookmark(
                    bookmark.id!!,
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
            val pageNotes = state.value.bookNoteEntities.filter {
                it.chapterNumber == textView.chapterNumber
                        && it.pageNumber == pageNumber
            }
            pageNotes.forEach { note ->
                textView.drawSingleNote(
                    note.id!!,
                    note.text,
                    note.start,
                    note.end,
                    false
                )
                Log.d("IHTextView", "page notes list = ${note.text}")
            }
        }
    }


    private fun getBookBookmarks(bookId: Int) {
        viewModelScope.launch {
            bookmarkUseCases.getBookmarksForBook(
                bookId,
            ).onEach { bookmarks ->
                _state.value = state.value.copy(
                    bookBookmarkEntities = bookmarks
                )
                Log.d("BookContentViewModel", "getBookmarksForBook is successful $bookmarks")
            }
                .launchIn(viewModelScope)
        }
    }

    private fun getBookHighlights(bookId: Int) {
        viewModelScope.launch {
            highlightUseCases.getBookHighlights(
                bookId,
            ).onEach { highlights ->
                _state.value = state.value.copy(
                    bookHighlightEntities = highlights
                )
                Log.d("BookContentViewModel", "getHighlightsForBook is successful $highlights")
            }
                .launchIn(viewModelScope)
        }
    }

    private fun getBookNotes(bookId: Int) {
        viewModelScope.launch {
            noteUseCases.getBookNotes(
                bookId,
            ).onEach { notes ->
                _state.value = state.value.copy(
                    bookNoteEntities = notes
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


    fun scrollToIndexLazy(
        targetPageIndex: Int,
        lazyListState: LazyListState,
        targetIndex: Int
    ) {
        val currentPosition = lazyListState.firstVisibleItemScrollOffset
        val currentItem = lazyListState.firstVisibleItemIndex
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

                    }

                    //use this to return to last position before navigation
//                    delay(2000)
//                    lazyListState.scrollToItem(currentItem)
//                    delay(200)
//                    lazyListState.scrollBy(currentPosition.toFloat())
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




