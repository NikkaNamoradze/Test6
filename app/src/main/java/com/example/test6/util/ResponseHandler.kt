package com.example.test6.util

sealed class ResponseHandler<T> {
    data class Success<T>(val data: T) : ResponseHandler<T>()
    data class Failure<T>(val errorMessage: String) : ResponseHandler<T>()
    data class Loading<T>(val loader: Boolean) : ResponseHandler<T>()
}
