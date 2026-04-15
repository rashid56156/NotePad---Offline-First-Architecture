package com.example.notepad.util

/**
 * A sealed class that wraps the result of any operation that can succeed, fail, or be loading.
 * Used throughout the app to represent UI state in a type-safe way.
 */
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()

    val isLoading get() = this is Loading
    val isSuccess get() = this is Success
    val isError get() = this is Error

    fun getOrNull(): T? = if (this is Success) data else null
    fun errorMessageOrNull(): String? = if (this is Error) message else null
}
