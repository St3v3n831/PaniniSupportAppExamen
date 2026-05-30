package com.panini.support.core
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val message: String = UserMessages.Network.UNKNOWN_ERROR,
        val statusCode: Int? = null
    ) : ApiResult<Nothing>()
}
