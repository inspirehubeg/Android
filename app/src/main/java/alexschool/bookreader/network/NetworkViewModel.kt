package alexschool.bookreader.network

import alexSchool.network.NetworkModule
import alexSchool.network.TableName
import alexSchool.network.domain.BookInfo
import alexSchool.network.domain.Category
import alexSchool.network.domain.DetailedBookInfo
import alexschool.bookreader.data.AppRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val appRepository: AppRepository,
) : ViewModel() {
    private val apiService = NetworkModule.provideApiService()
    val defaultDispatcher = NetworkModule.provideDispatcher()
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()
    private val _books = MutableStateFlow<List<BookInfo>>(emptyList())
    val books = _books.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<AppError?>(null)
    val error: StateFlow<AppError?> = _error.asStateFlow()

    var detailedBookInfo: DetailedBookInfo? = null

    init {
        fetchData()
//        fetchCategories()
//        fetchBooks()
//        fetchDetailedBookInfo(1)
    }

    private fun fetchData() {
        viewModelScope.launch {
            _isLoading.value = true
            combine(
                appRepository.getCategories(),
                appRepository.getRemoteBooks()
            ) { categories, books ->
                _categories.value = categories
                _books.value = books
            }.catch { e ->
                _error.value = AppError.NetworkError(e as Exception) // Or other error type
            }.collect {
                _isLoading.value = false
            }
        }
    }
    //    private fun fetchBookInfo() {
//        viewModelScope.launch {
//            apiService.getBookInfo()
//                .flowOn(defaultDispatcher)
//                .catch {e ->
//                    val error = when (e) {
//                        is ConnectException -> AppError.NetworkError("Network connection error")
//                        is HttpRequestTimeoutException -> AppError.NetworkError("Request timeout")
//                        is ClientRequestException -> AppError.HttpError(e.response.status.value, "HTTP error")
//                        is ServerResponseException -> AppError.HttpError(e.response.status.value, "Server error")
//                        //is JsonDecodingException -> AppError.SerializationError("JSON decoding error")
//                        else -> AppError.ApplicationError("Something went wrong")
//                    }
//                    _bookInfo.value = ApiResult.Error(error)
//
//                    //_bookInfo.value = ApiResult.Error(it.message ?: "Something went wrong")
//                }
//                .collect {
//                    _bookInfo.value = it
//                }
//        }
//    }


//    private fun fetchBookInfo(){
//        viewModelScope.launch {
//            appRepository.getBookInfo().catch { e ->
//                // Handle errors, e.g., log the error or update an error state
//                Log.e("NetworkViewModel", "Error fetching book info: ${e.message}")
//        }.collect { bookInfos ->
//            _bookInfo.value = bookInfos
//        }
//        }
//    }

    private fun fetchGeneralChanges() {
        viewModelScope.launch {
            val generalChanges = appRepository.getGeneralChanges()
            for (change in generalChanges) {
                when (change) {
                    TableName.BOOKS -> fetchBooks()
                    TableName.CATEGORIES -> fetchCategories()
                    TableName.TAGS -> fetchTags()
                    TableName.AUTHORS -> fetchAuthors()
                    TableName.TRANSLATORS -> fetchTranslators()
                    TableName.READING_PROGRESS -> fetchReadingProgress()
                    TableName.SUBSCRIPTIONS -> fetchSubscriptions()
                    else -> {}
                }
            }
        }
    }

    private fun fetchSubscriptions() {
        TODO("Not yet implemented")
    }

    private fun fetchReadingProgress() {
        TODO("Not yet implemented")
    }

    private fun fetchTranslators() {
        TODO("Not yet implemented")
    }

    private fun fetchAuthors() {
        TODO("Not yet implemented")
    }

    private fun fetchTags() {
        TODO("Not yet implemented")
    }



    private fun fetchBooks() {
        viewModelScope.launch {
            appRepository.getRemoteBooks().catch { e ->
                // Handle errors, e.g., log the error or update an error state
                Log.e("NetworkViewModel", "Error fetching books: ${e.message}")
            }.collect { books ->
                _books.value = books
            }
        }
    }

    private fun fetchDetailedBookInfo(bookId: Int) {
        viewModelScope.launch {
            detailedBookInfo = appRepository.getDetailedBookInfo(bookId)
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            appRepository.getCategories().catch { e ->
                // Handle errors, e.g., log the error or update an error state
                Log.e("NetworkViewModel", "Error fetching categories: ${e.message}")
            }.collect { categories ->
                _categories.value = categories
            }
        }
    }
}