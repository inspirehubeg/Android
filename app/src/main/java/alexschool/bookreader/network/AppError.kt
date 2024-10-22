package alexschool.bookreader.network

sealed class AppError {
    data class NetworkError(val exception: Exception) : AppError()
    data class DatabaseError(val exception: Exception) : AppError()
    data class ApiError(val statusCode: Int, val message: String) : AppError()
}