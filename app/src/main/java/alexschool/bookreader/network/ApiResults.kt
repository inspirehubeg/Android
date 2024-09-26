package alexschool.bookreader.network

sealed class ApiResult<out T>(val data: T? = null, val error: AppError? = null/* val message: String? = null*/) {
    class Success<T>(data: T) : ApiResult<T>(data)
    class Error<T>(error: AppError) : ApiResult<T>(error = error)
  //  class Error<T>(message: String, data: T? = null) : ApiResult<T>(data, message)
    class Loading<T> : ApiResult<T>()
}

sealed class AppError(val message: String) {
    class NetworkError(message: String) : AppError(message)
    class HttpError(val statusCode: Int, message: String) : AppError(message)
    class SerializationError(message: String) : AppError(message)
    class ApplicationError(message: String) : AppError(message)
}