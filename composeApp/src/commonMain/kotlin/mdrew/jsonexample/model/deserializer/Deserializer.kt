package mdrew.jsonexample.model.deserializer

import mdrew.jsonexample.Error

fun interface Deserializer<T> {
    fun deserialize(input: String?): Result<T>

    sealed class Result<T> {
        data class Success<T>(val data: T): Result<T>()
        data class Failure<T>(val reason: Reason, val exception:Exception? = null): Result<T>() {
            enum class Reason: Error {
                MALFORMED,
                OTHER
            }
        }
    }
}