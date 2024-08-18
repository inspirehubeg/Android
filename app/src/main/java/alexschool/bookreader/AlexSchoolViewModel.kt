package alexschool.bookreader

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


//@HiltViewModel

class AlexSchoolViewModel : ViewModel() {

    private val _currentScreen= MutableStateFlow<String?>(null)
    val currentScreen: StateFlow<String?> = _currentScreen.asStateFlow()

    fun setCurrentScreen(screenName: String?) {
        _currentScreen.value = screenName
    }
}