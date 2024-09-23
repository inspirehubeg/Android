package ih.tools.readingpad.ui

import android.util.Log
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ih.tools.readingpad.feature_book_parsing.data.PreferencesManager
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkClickableSpan
import ih.tools.readingpad.ui.theme.HighlightLaserLemon
import ih.tools.readingpad.util.IHNoteClickableSpan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class UIStateViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // UI Settings
    data class UISettings(
        val isVerticalScroll: Boolean,
        val isOneFingerScroll: Boolean,
        val isDrawerOpen: Boolean,
        val areDrawerGesturesEnabled: Boolean,
        val showHighlightsBookmarks: Boolean,
        val keepScreenOn: Boolean,
        val pinnedTopBar: Boolean,
        val showSearchBar: Boolean,
        val darkTheme: Boolean,
        val fontSize: Float,
        val fontWeight: Int,
        val fontColor: Int,
        val backgroundColor: Int
    )

    private val _uiSettings = MutableStateFlow(
        UISettings(
            isVerticalScroll = false,
            isOneFingerScroll = true,
            isDrawerOpen = false,
            areDrawerGesturesEnabled = true,
            showHighlightsBookmarks = true,
            keepScreenOn = false,
            pinnedTopBar = preferencesManager.isPinnedTopBar(),
            showSearchBar = false,
            darkTheme = preferencesManager.isDarkTheme(),
            fontSize = preferencesManager.getFontSize(),
            fontWeight = preferencesManager.getFontWeight(),
            fontColor = preferencesManager.getFontColor(),
            backgroundColor = preferencesManager.getBackgroundColor()
        )
    )
    val uiSettings = _uiSettings.asStateFlow()

    private val _topBarHeight = MutableStateFlow(0f)
    val topBarHeight = _topBarHeight.asStateFlow()
    fun setTopBarHeight(height: Float) {
        _topBarHeight.value = height
    }

    private val _bottomBarHeight = MutableStateFlow(0f)
    val bottomBarHeight = _bottomBarHeight.asStateFlow()
    fun setBottomBarHeight(height: Float) {
        _bottomBarHeight.value = height
    }
    private val _screenWidth = MutableStateFlow(0f)
    val screenWidth = _screenWidth.asStateFlow()

    private val _menuWidth = MutableStateFlow(0f)
    val menuWidth = _menuWidth.asStateFlow()

    fun setScreenWidth(width: Float) {
        _screenWidth.value = width
    }

    fun setMenuWidth(width: Float) {
        _menuWidth.value = width
    }
    // Dialogs
    private val _currentDialog = MutableStateFlow<DialogType?>(null)
    val currentDialog = _currentDialog.asStateFlow()

    // Screens
    private val _currentScreen = MutableStateFlow<ScreenType?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    // Other UI State
    private val _showTopBar = MutableStateFlow(true)
    val showTopBar = _showTopBar.asStateFlow()

    private val _imageClicked = MutableStateFlow<ByteArray?>(null)
    val imageClicked = _imageClicked.asStateFlow()
    private val _preferredHighlightColor = MutableStateFlow(HighlightLaserLemon)
    val preferredHighlightColor: StateFlow<Color> = _preferredHighlightColor.asStateFlow()

    private val _bookmarkClickEvent = MutableStateFlow<IHBookmarkClickableSpan?>(null)
    val bookmarkClickEvent: StateFlow<IHBookmarkClickableSpan?> =
        _bookmarkClickEvent.asStateFlow()

    private val _noteClickEvent = MutableStateFlow<IHNoteClickableSpan?>(null)
    val noteClickEvent: StateFlow<IHNoteClickableSpan?> = _noteClickEvent.asStateFlow()

    private val _editNote = MutableStateFlow(false)
    val editNote: StateFlow<Boolean> = _editNote.asStateFlow()

    private val _noteText = MutableStateFlow("")
    val noteText: StateFlow<String> = _noteText
    private val _noteEnd = MutableStateFlow(0)
    val noteEnd: StateFlow<Int> = _noteEnd

    private val _noteStart = MutableStateFlow(0)
    val noteStart: StateFlow<Int> = _noteStart
    private val _notePageNumber = MutableStateFlow(0)
    val notePageNumber: StateFlow<Int> = _notePageNumber

    private val _imageRotation = MutableStateFlow(0f)
    val imageRotation: StateFlow<Float> = _imageRotation.asStateFlow()

    private val _editBookmark = MutableStateFlow(false)
    val editBookmark: StateFlow<Boolean> = _editBookmark.asStateFlow()

    private val _bookmarkEnd = MutableStateFlow(0)
    val bookmarkEnd: StateFlow<Int> = _bookmarkEnd

    private val _bookmarkStart = MutableStateFlow(0)
    val bookmarkStart: StateFlow<Int> = _bookmarkStart

    private val _bookmarkName = MutableStateFlow("")
    val bookmarkName: StateFlow<String> = _bookmarkName
    private val _bookmarkPageNumber = MutableStateFlow(0)
    val bookmarkPageNumber: StateFlow<Int> = _bookmarkPageNumber

    private val _brightnessLevel = MutableStateFlow(preferencesManager.getBrightnessLevel())
    val brightnessLevel: StateFlow<Float> = _brightnessLevel.asStateFlow()

//    private val _fontSizeChanged = MutableStateFlow(false)
//    val fontSizeChanged: StateFlow<Boolean> = _fontSizeChanged


    private val _showCustomSelectionMenu = MutableStateFlow(false)
    val showCustomSelectionMenu: StateFlow<Boolean> = _showCustomSelectionMenu.asStateFlow()
    fun setShowCustomSelectionMenu(show: Boolean) {
        _showCustomSelectionMenu.value = show
    }

    // Helper functions to trigger UI events
    fun showDialog(dialogType: DialogType?) {
        viewModelScope.launch {
            _currentDialog.value = dialogType
            _currentScreen.value = null
            _imageClicked.value = null
        }
    }

    fun showScreen(screenType: ScreenType?) {
        viewModelScope.launch {
            _currentScreen.value = screenType
            _currentDialog.value = null
        }
    }

    fun updateUISettings(settings: UISettings) {
        viewModelScope.launch {
            _uiSettings.value = settings
        }
    }

//    fun setKeepScreenOn(keepOn: Boolean) {
//        viewModelScope.launch {
//            _keepScreenOn.value = keepOn
//        }
//    }

    fun onImageClick(imageData: ByteArray?) {
        viewModelScope.launch {
            _imageClicked.value = imageData
            _imageRotation.value = 0f
            setAreDrawerGesturesEnabled(false)
            toggleTopBar(true)
        }
    }

    fun toggleTopBar(value: Boolean) {
        viewModelScope.launch {
            _showTopBar.value = value
            if (!value && currentDialog.value == DialogType.PagesSlider) {
                // setShowPagesSlider(false)
                Log.d("onTouch", "hide page slider")
                _currentDialog.value = null
            }
        }
    }
//    fun setShowPagesSlider(show: Boolean) {
//        viewModelScope.launch {
//            _showPageSlider.value = show
//        }
//    }

    fun setPreferredHighlightColor(color: Color) {
        viewModelScope.launch {
            _preferredHighlightColor.value = color
        }
    }

//    fun setSelectedSpans(spans: List<IHSpan>) {
//        viewModelScope.launch {
//            _selectedSpans.value = spans
//        }
//    }

    fun setBookmarkClickEvent(span: IHBookmarkClickableSpan?) {
        viewModelScope.launch {
            _bookmarkClickEvent.value = span
        }
    }

    fun setNoteClickEvent(span: IHNoteClickableSpan?) {
        viewModelScope.launch {
            _noteClickEvent.value = span
        }
    }

    fun setEditNote(edit: Boolean) {
        viewModelScope.launch {
            _editNote.value = edit
        }
    }

    fun setNoteText(text: String) {
        viewModelScope.launch {
            _noteText.value = text
        }
    }

    fun setNoteEnd(end: Int) {
        viewModelScope.launch {
            _noteEnd.value = end

        }
    }

    fun setBrightnessValue(value: Float) {
        viewModelScope.launch {
            preferencesManager.setBrightnessLevel(value)
            _brightnessLevel.value = value
        }
    }

    fun setNoteStart(start: Int) {
        viewModelScope.launch {
            _noteStart.value = start
        }
    }

    fun setImageRotation(rotation: Float) {
        viewModelScope.launch {
            _imageRotation.value = rotation
        }
    }

    fun setEditBookmark(edit: Boolean) {
        viewModelScope.launch {
            _editBookmark.value = edit
        }
    }

    private val _currentThemeFontColor = MutableLiveData(uiSettings.value.fontColor)
    val currentThemeFontColor = _currentThemeFontColor
    fun setCurrentThemeFontColor(colorInt: Int) {
        _currentThemeFontColor.value = colorInt
    }

    private val _currentThemeBackgroundColor = MutableLiveData(uiSettings.value.backgroundColor)
    val currentThemeBackgroundColor = _currentThemeBackgroundColor
    fun setCurrentThemeBackgroundColor(colorInt: Int) {
        _currentThemeBackgroundColor.value = colorInt
    }

    private val _fontSizeChanged = MutableStateFlow(false)
    val fontSizeChanged: StateFlow<Boolean> = _fontSizeChanged

    fun setFontSizeChanged(value: Boolean) {
        _fontSizeChanged.value = value
    }

    var lazyListState = LazyListState()
    private val _currentPageIndex = MutableStateFlow(0)
    val currentPageIndex: StateFlow<Int> = _currentPageIndex

    fun setBookmarkName(name: String) {
        viewModelScope.launch {
            _bookmarkName.value = name
        }
    }

    fun setBookmarkData(start: Int, name: String, end: Int, pageNumber: Int) {
        viewModelScope.launch {
            _bookmarkStart.value = start
            _bookmarkName.value = name
            _bookmarkEnd.value = end
            _bookmarkPageNumber.value = pageNumber
        }
    }

    fun setNoteData(start: Int, end: Int, pageNumber: Int) {
        viewModelScope.launch {
            _noteStart.value = start
            _noteEnd.value = end
            _notePageNumber.value = pageNumber
        }
    }


    fun setIsOneFingerScroll(isOneFingerScroll: Boolean) {
        viewModelScope.launch {
            _uiSettings.value = _uiSettings.value.copy(isOneFingerScroll = isOneFingerScroll)
        }
    }

    fun setIsDrawerOpen(isDrawerOpen: Boolean) {
        viewModelScope.launch {
            _uiSettings.value = _uiSettings.value.copy(isDrawerOpen = isDrawerOpen)
        }
    }

    fun setAreDrawerGesturesEnabled(areDrawerGesturesEnabled: Boolean) {
        viewModelScope.launch {
            _uiSettings.value =
                _uiSettings.value.copy(areDrawerGesturesEnabled = areDrawerGesturesEnabled)
        }
    }

    fun setShowHighlightsBookmarks(showHighlightsBookmarks: Boolean) {
        viewModelScope.launch {
            _uiSettings.value =
                _uiSettings.value.copy(showHighlightsBookmarks = showHighlightsBookmarks)
        }
    }


    fun setIsVerticalScroll(isVerticalScroll: Boolean) {
        viewModelScope.launch {
            preferencesManager.setVerticalScroll(isVerticalScroll)
            _uiSettings.value = _uiSettings.value.copy(isVerticalScroll = isVerticalScroll)
        }
    }


    fun setPinnedTopBar(pinnedTopBar: Boolean) {
        viewModelScope.launch {
            preferencesManager.setPinnedTopBar(pinnedTopBar)
            _uiSettings.value = _uiSettings.value.copy(pinnedTopBar = pinnedTopBar)
        }
    }

    fun setDarkTheme(darkTheme: Boolean, textColor: Int, backgroundColor: Int) {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(darkTheme)
            _uiSettings.value = _uiSettings.value.copy(darkTheme = darkTheme)
            setFontColor(textColor)
            setBackgroundColor(backgroundColor)
        }
    }


    fun setFontSize(fontSize: Float, currentY: Float) {
        _currentPageIndex.value = lazyListState.firstVisibleItemIndex
        val fontSizePercentage = fontSize / uiSettings.value.fontSize

        preferencesManager.setFontSize(fontSize)
        _uiSettings.value = _uiSettings.value.copy(fontSize = fontSize)
        var y = 0
        y = lazyListState.firstVisibleItemScrollOffset

        val newY = y * fontSizePercentage * 1.01f
        scrollToIndex((newY - y))
        _fontSizeChanged.value = true
    }

    private fun scrollToIndex(targetPageIndex: Float) {
        viewModelScope.launch {
            lazyListState.scrollBy(targetPageIndex)
        }
    }

    fun setFontWeight(fontWeight: Int) {
        viewModelScope.launch {
            preferencesManager.setFontWeight(fontWeight)
            _uiSettings.value = _uiSettings.value.copy(fontWeight = fontWeight)
        }
    }

    fun setFontColor(fontColor: Int) {
        viewModelScope.launch {
            preferencesManager.setFontColor(fontColor)
            _uiSettings.value = _uiSettings.value.copy(fontColor = fontColor)
        }
    }

    fun setBackgroundColor(backgroundColor: Int) {
        viewModelScope.launch {
            preferencesManager.setBackgroundColor(backgroundColor)
            _uiSettings.value = _uiSettings.value.copy(backgroundColor = backgroundColor)
        }
    }

    private val _textView = MutableStateFlow<IHTextView?>(null)
    val textView: StateFlow<IHTextView?> = _textView
    fun setTextView(textView: IHTextView) {
        _textView.value = textView
    }

    private val _selectionStart = MutableStateFlow(-1)
    val selectionStart: StateFlow<Int> = _selectionStart
    fun setSelectionStart(start: Int) {
        _selectionStart.value = start
    }

    fun setSelectionEnd(end: Int) {
        _selectionEnd.value = end
    }

    private val _selectionEnd = MutableStateFlow(-1)
    val selectionEnd: StateFlow<Int> = _selectionEnd

    private val _menuPosition = MutableStateFlow(IntOffset(0, 0))
    val menuPosition: StateFlow<IntOffset> = _menuPosition
    fun setMenuPosition(position: IntOffset) {
        Log.d("SelectionMenu", "setMenuPosition called with position: $position")
        if (textView.value != null) {
            val key = textView.value?.itemKey // Access the item key from the TextView
            Log.d("SelectionMenu", "textView.value is not null, key: $key")
            val itemIndex = lazyListState.layoutInfo.visibleItemsInfo.indexOfFirst { it.key == key }
            Log.d("SelectionMenu", "itemIndex: $itemIndex")
            if (itemIndex != -1) {
                Log.d("SelectionMenu", "topBar = ${topBarHeight.value.roundToInt()}")
                val itemInfo = lazyListState.layoutInfo.visibleItemsInfo[itemIndex]

                val itemTop = itemInfo.offset

                val paddingStart = textView.value!!.paddingStart
                val paddingTop = textView.value!!.paddingTop
                Log.d("SelectionMenu", "paddingStart: $paddingStart, paddingTop: $paddingTop")


                val topBarHeightInt = if (showTopBar.value || uiSettings.value.pinnedTopBar) {
                    topBarHeight.value.roundToInt()
                } else {
                    0 // No top bar height when not pinned
                }

                val bottomBarHeightInt = if (showTopBar.value || uiSettings.value.pinnedTopBar) {
                    bottomBarHeight.value.roundToInt()
                } else {
                    0 // No bottom bar height when not pinned
                }

                val selectionY = if (uiSettings.value.pinnedTopBar){
                    position.y + itemTop
                } else{
                    position.y + itemTop - topBarHeight.value.roundToInt()
                }

                Log.d("SelectionMenu", "selectionY: ${position.y} + $itemTop")
                //to position in middle of the line
                //val menuX = (screenWidth.value/2 - (menuWidth.value/2))
                    //position.x - paddingStart

                //to position in the middle of the selection
                val menuX = position.x
                Log.d("SelectionMenu", "screen width = $screenWidth, menuWidth = $menuWidth")
                val menuY = selectionY + paddingTop + topBarHeightInt - bottomBarHeightInt
                Log.d(
                    "SelectionMenu",
                    "topBarHeight: $topBarHeightInt, bottomBarHeight: $bottomBarHeightInt"
                )

                val menuPosition = IntOffset(menuX.toInt(), menuY/*+100*/)
                Log.d("SelectionMenu", "menuPosition: $menuPosition")
                _menuPosition.value = menuPosition
            } else {
                Log.d("SelectionMenu", "itemIndex is -1")
            }
        } else {
            Log.d("SelectionMenu", "textView.value is null")
        }
    }

    fun clearSelection() {
        setSelectionStart(-1)
        setSelectionEnd(-1)
    }


    // Enum classes for dialog and screen types
    enum class DialogType {
        Brightness, BookmarkList, PageNumber, AddBookmark, EditBookmark,
        AddNote, EditNote, ThemeSelector, FontSlider, PagesSlider// ... other dialog types
    }

    enum class ScreenType {
        UserInput, CustomTheme, FullScreenImage,
        // ... other screen types
    }
}