package com.mobikasa.samplemvvm.utils

import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
    data class Loading(val data: Nothing?) : NetworkResponse<Nothing, Nothing>()
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T?) : NetworkResponse<T, Nothing>()

    /**
     * Failure response with body
     */
    data class ApiError<U : Any>(val body: U?, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException?) : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}