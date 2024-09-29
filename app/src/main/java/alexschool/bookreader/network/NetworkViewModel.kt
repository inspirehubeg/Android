package alexschool.bookreader.network

import alexschool.bookreader.data.AppRepository
import alexschool.bookreader.domain.BookInfo
import alexschool.bookreader.domain.Category
import alexschool.bookreader.domain.PostResponse
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val apiService: ApiService,
    private val appRepository: AppRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {


    private val _postResponses = MutableStateFlow<List<PostResponse>>(emptyList())
    val postResponses = _postResponses.asStateFlow()

    private val _bookInfo = MutableStateFlow<List<BookInfo>>(emptyList())
    val bookInfo = _bookInfo.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        fetchBookInfo()
        fetchCategories()
        //fetchPostResponses()
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
    private fun fetchPostResponses() {
        viewModelScope.launch {
            _postResponses.value = apiService.getPosts()

        }
    }

    private fun fetchBookInfo(){
        viewModelScope.launch {
            appRepository.getBookInfo().catch { e ->
                // Handle errors, e.g., log the error or update an error state
                Log.e("NetworkViewModel", "Error fetching book info: ${e.message}")
        }.collect { bookInfos ->
            _bookInfo.value = bookInfos
        }
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