package com.binh.core.data.util

import com.binh.core.data.network.NoConnectivityException
import com.binh.core.data.network.RefreshTokenUnAuthorizedException
import com.binh.core.data.network.RefreshTokenUnknownException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

sealed class Failure(val throwable: Throwable? = null) {

    class NoConnection : Failure()

    class ConnectionTimeout : Failure()

    class Timeout : Failure()

    class Unknown(throwable: Throwable?) : Failure(throwable)

    class BadRequest(throwable: Throwable?, val msg: String? = null) : Failure(throwable)

    class UnAuthorized : Failure()

    class RefreshTokenFailure : Failure()

    class Forbidden : Failure()

    class HttpNotFound : Failure()

    class HttpFailure(throwable: Throwable?) : Failure(throwable)
}

val Throwable?.asFailure: Failure
    get() = when (this) {
        is HttpException -> {
            val response = response()
            when {
                response?.code() == 400 -> Failure.BadRequest(this)
                response?.code() == 401 -> Failure.UnAuthorized()
                response?.code() == 403 -> Failure.Forbidden()
                response?.code() == 404 -> Failure.HttpNotFound()
                else -> Failure.HttpFailure(this)
            }
        }

        is NoConnectivityException -> Failure.NoConnection()

        is SocketTimeoutException,
        is UnknownHostException,
        is InterruptedIOException -> Failure.ConnectionTimeout()

        is RefreshTokenUnAuthorizedException -> Failure.UnAuthorized()

        is RefreshTokenUnknownException -> Failure.RefreshTokenFailure()

        is TimeoutException -> Failure.Timeout()

        else -> Failure.Unknown(this)
    }


