package alexschool.bookreader.network

import alexschool.bookreader.network.model.dto.BookInfo
import alexschool.bookreader.network.model.dto.Category
import alexschool.bookreader.network.model.dto.PostResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val apiService: ApiService,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {


    private val _postResponses = MutableStateFlow<List<PostResponse>>(emptyList())
    val postResponses = _postResponses.asStateFlow()

    private val _bookInfo = MutableStateFlow<ApiResult<List<BookInfo>>>(ApiResult.Loading())
    val bookInfo = _bookInfo.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
//        fetchBookInfo()
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
    private fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = apiService.getCategories()

        }
    }
}