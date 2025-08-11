package com.binh.core.data.network

import com.binh.core.data.util.Failure
import com.binh.core.data.util.asFailure
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class NetworkResponse<T>(val data: T)

@JsonClass(generateAdapter = true)
open class GenericNetworkResponse<T>(
    val data: T? = null,
    val status: Boolean? = null,
    val message: String? = null,
    val error: String? = null
) {
    val isSuccessful get() = status == true && data != null

    inline fun <T> GenericNetworkResponse<T>.onSuccess(action: (value: T) -> Unit): GenericNetworkResponse<T> {
        if (isSuccessful) action(data!!)
        return this
    }

    inline fun <T> GenericNetworkResponse<T>.onFailure(action: (exception: Failure) -> Unit): GenericNetworkResponse<T> {
        if (!isSuccessful) action(Throwable(error).asFailure)
        return this
    }
}

@JsonClass(generateAdapter = true)
class SuccessNetworkResponse<T>(
    val status: Boolean? = null,
    val message: String? = null,
    val error: String? = null
) {
    val isSuccessful get() = status == true
    inline fun <T> SuccessNetworkResponse<T>.onSuccess(action: () -> Unit): SuccessNetworkResponse<T> {
        if (isSuccessful) action()
        return this
    }

    inline fun <T> SuccessNetworkResponse<T>.onFailure(action: (exception: Failure) -> Unit): SuccessNetworkResponse<T> {
        if (!isSuccessful) action(Throwable(error).asFailure)
        return this
    }
}



