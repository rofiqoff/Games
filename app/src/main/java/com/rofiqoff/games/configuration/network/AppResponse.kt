package com.rofiqoff.games.configuration.network

sealed interface AppResponse<out T> {
    open class Error(open val message: String) : AppResponse<Nothing>

    class Success<T>(val data: T) : AppResponse<T>

    companion object {
        fun <T> createSuccess(data: T): AppResponse<T> = Success(data)
        fun <T> createError(message: String): AppResponse<T> = Error(message)
    }
}
