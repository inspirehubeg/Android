package ih.tools.readingpad.ui

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ih.tools.readingpad.feature_book_parsing.data.PreferencesManager
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView
import ih.tools.readingpad.feature_bookmark.presentation.IHBookmarkClickableSpan
import ih.tools.readingpad.ui.theme.HighlightLaserLemon
import ih.tools.readingpad.util.IHNoteClickableSpan
import ih.tools.readingpad.util.IHSpan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
            darkTheme = preferencesManager.isDarkTheme(),
            fontSize = preferencesManager.getFontSize(),
            fontWeight = preferencesManager.getFontWeight(),
            fontColor = preferencesManager.getFontColor(),
            backgroundColor = preferencesManager.getBackgroundColor()
        )
    )
    val uiSettings = _uiSettings.asStateFlow()

    // Dialogs
    private val _currentDialog = MutableStateFlow<DialogType?>(null)
    val currentDialog = _currentDialog.asStateFlow()

    // Screens
    private val _currentScreen = MutableStateFlow<ScreenType?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    // Other UI State
    private val _showTopBar = MutableStateFlow(true)
    val showTopBar = _showTopBar.asStateFlow()
    private val _showPageSlider = MutableStateFlow(false)
    val showPageSlider = _showPageSlider.asStateFlow()

    private val _keepScreenOn = MutableStateFlow(false)
    val keepScreenOn = _keepScreenOn.asStateFlow()

    private val _imageClicked = MutableStateFlow<ByteArray?>(null)
    val imageClicked = _imageClicked.asStateFlow()
    private val _preferredHighlightColor = MutableStateFlow(HighlightLaserLemon)
    val preferredHighlightColor: StateFlow<Color> = _preferredHighlightColor.asStateFlow()

    private val _selectedSpans = MutableStateFlow<List<IHSpan>>(emptyList())
    val selectedSpans: StateFlow<List<IHSpan>> = _selectedSpans.asStateFlow()

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

    private val _brightnessValue = MutableStateFlow(0.5f)
    val brightnessValue: StateFlow<Float> = _brightnessValue.asStateFlow()

    private val _textView = MutableStateFlow<IHTextView?>(null)
    val textView: StateFlow<IHTextView?> = _textView

    private val _fontSizeChanged = MutableStateFlow(false)
    val fontSizeChanged: StateFlow<Boolean> = _fontSizeChanged

    private val _linkNavigationPage = MutableStateFlow(false)
    val linkNavigationPage: StateFlow<Boolean> = _linkNavigationPage

    private val _showCustomSelectionMenu = MutableStateFlow(false)
    val showCustomSelectionMenu: StateFlow<Boolean> = _showCustomSelectionMenu.asStateFlow()
    fun setShowCustomSelectionMenu(show: Boolean) {
        _showCustomSelectionMenu.value = show
    }

    // Helper functions to trigger UI events
    fun showDialog(dialogType: DialogType?) {
        viewModelScope.launch {
            _currentDialog.value = dialogType
        }
    }

    fun showScreen(screenType: ScreenType?) {
        viewModelScope.launch {
            _currentScreen.value = screenType
        }
    }

    fun updateUISettings(settings: UISettings) {
        viewModelScope.launch {
            _uiSettings.value = settings
        }
    }

    fun setKeepScreenOn(keepOn: Boolean) {
        viewModelScope.launch {
            _keepScreenOn.value = keepOn
        }
    }

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
            if (!value && currentDialog.value == DialogType.PagesSlider){
               // setShowPagesSlider(false)
                Log.d("onTouch", "hide page slider")
                _currentDialog.value = null
            }
        }
    }
    fun setShowPagesSlider(show: Boolean) {
        viewModelScope.launch {
            _showPageSlider.value = show
        }
    }

    fun setPreferredHighlightColor(color: Color) {
        viewModelScope.launch {
            _preferredHighlightColor.value = color
        }
    }

    fun setSelectedSpans(spans: List<IHSpan>) {
        viewModelScope.launch {
            _selectedSpans.value = spans
        }
    }

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
            _brightnessValue.value = value
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


    fun setTextView(textView: IHTextView) {
        viewModelScope.launch {
            _textView.value = textView
        }
    }

    fun setFontSizeChanged(value: Boolean) {
        viewModelScope.launch {
            _fontSizeChanged.value = value
        }
    }

    fun setLinkNavigationPage(value: Boolean) {
        viewModelScope.launch {
            _linkNavigationPage.value = value
        }
    }

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
            _uiSettings.value = _uiSettings.value.copy(areDrawerGesturesEnabled = areDrawerGesturesEnabled)
        }
    }
    fun setShowHighlightsBookmarks(showHighlightsBookmarks: Boolean) {
        viewModelScope.launch {
            _uiSettings.value = _uiSettings.value.copy(showHighlightsBookmarks = showHighlightsBookmarks)
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

    fun setDarkTheme(darkTheme: Boolean ,textColor: Int, backgroundColor: Int) {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(darkTheme)
            _uiSettings.value = _uiSettings.value.copy(darkTheme = darkTheme)
            setFontColor(textColor)
            setBackgroundColor(backgroundColor)
        }
    }

    fun setFontSize(fontSize: Float) {
        viewModelScope.launch {
            preferencesManager.setFontSize(fontSize)
            _uiSettings.value = _uiSettings.value.copy(fontSize = fontSize)
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

    // Enum classes for dialog and screen types
    enum class DialogType {
        Brightness, BookmarkList, PageNumber, AddBookmark, EditBookmark,
        AddNote, EditNote,ThemeSelector, FontSlider, PagesSlider// ... other dialog types
    }

    enum class ScreenType {
        UserInput, CustomTheme, FullScreenImage,
         // ... other screen types
    }

    init {

    }
}