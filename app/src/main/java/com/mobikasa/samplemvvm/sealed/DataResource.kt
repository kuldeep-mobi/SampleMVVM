package com.mobikasa.samplemvvm.sealed

sealed class DataResource<out T : Any>

data class Loading(val loader: Int) : DataResource<Nothing>()
data class Success<out T : Any>(val data: T?) : DataResource<T>()
data class Error(val errorMessage: String?) : DataResource<Nothing>()


sealed class HttpErrors : DataResource<Nothing>()

data class HttpErrorCodes(val responseCode: Int?) : HttpErrors()



