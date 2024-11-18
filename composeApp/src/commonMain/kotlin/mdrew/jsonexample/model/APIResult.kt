package mdrew.jsonexample.model

import mdrew.jsonexample.Error

sealed class APIResult<T> {
    data class Success<T>(val data: T): APIResult<T>()
    data class Failure<T>(
        val message: String? = "",
        val errorCode: Int? = null,
        val cause: Error? = null,
        val data: T? = null
    ): APIResult<T>()
}