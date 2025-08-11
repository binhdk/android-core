package com.binh.core.data

import android.util.Log
import com.binh.core.data.util.Failure
import com.binh.core.data.util.asFailure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Failure(val exception: Throwable? = null) : Result<Nothing>
    data object Loading : Result<Nothing>

    val isSuccess
        get() = this is Success

    val isFailure
        get() = this is Failure

    val isLoading
        get() = this is Loading


    fun getOrNull(): T? =
        when (this) {
            is Success -> data
            else -> null
        }

    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Failure -> exception
            else -> null
        }
}

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}


inline fun <T> Result<T>.onFailure(action: (exception: Failure) -> Unit): Result<T> {
    if (this is Result.Failure) action(this.exception.asFailure)
    return this
}


inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}

inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable?) -> R,
    onLoading: () -> R
): R {
    return when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Failure -> onFailure(exception)
        is Result.Loading -> onLoading()
    }
}

inline fun <R> catch(block: () -> R): Result<R> {
    return try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Failure(e)
    }
}

inline fun <T, R> T.catch(block: T.() -> R): Result<R> {
    return try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Failure(e)
    }
}

suspend inline fun <T> result(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Log.e("ERROR", "e", e)
        Result.Failure(e)
    }
}

suspend inline fun <T, R> result(
    crossinline block: suspend () -> T,
    crossinline transform: T.() -> R
): Result<R> {
    return try {
        Result.Success(block().transform())
    } catch (e: Exception) {
        Log.e("ERROR", "e", e)
        Result.Failure(e)
    }
}

suspend inline fun <T> result(
    crossinline block: suspend () -> T,
    dispatcher: CoroutineDispatcher
): Result<T> {
    return try {
        withContext(dispatcher) {
            Result.Success(block())
        }
    } catch (e: Exception) {
        Log.e("ERROR", "e", e)
        Result.Failure(e)
    }
}

suspend inline fun <T> result(
    crossinline block: suspend () -> T,
    crossinline transform: T.() -> R,
    dispatcher: CoroutineDispatcher
): Result<R> {
    return try {
        withContext(dispatcher) {
            Result.Success(block().transform())
        }
    } catch (e: Exception) {
        Log.e("ERROR", "e", e)
        Result.Failure(e)
    }
}

suspend inline fun <T> ioResult(
    crossinline block: suspend () -> T
): Result<T> {
    return result(block, Dispatchers.IO)
}

suspend inline fun <T> ioResult(
    crossinline block: suspend () -> T,
    crossinline transform: T.() -> R
): Result<R> {
    return result(block, transform, Dispatchers.IO)
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch {
            Log.e("ERROR", "e", it)
            emit(Result.Failure(it))
        }
}

suspend inline fun <T> Flow<Result<T>>.onSuccess(crossinline action: suspend (T) -> Unit) {
    collect {
        if (it is Result.Success)
            action(it.data)
    }
}

inline fun <T> Flow<Result<T>>.catchFailure(crossinline action: suspend (Failure) -> Unit): Flow<Result<T>> {
    return onEach {
        if (it is Result.Failure)
            action(it.exception.asFailure)
    }.catch {
        action(it.asFailure)
    }
}

inline fun <T> resultFlow(
    crossinline block: suspend () -> T,
): Flow<Result<T>> = flow { emit(block()) }.asResult()

inline fun <T, R> resultFlow(
    crossinline block: suspend () -> T,
    crossinline transform: T.() -> R
): Flow<Result<R>> = flow { emit(block().transform()) }.asResult()

inline fun <T> resultFlow(
    crossinline block: suspend () -> T,
    dispatcher: CoroutineDispatcher
): Flow<Result<T>> = flow { emit(block()) }.asResult().flowOn(dispatcher)

inline fun <T, R> resultFlow(
    crossinline block: suspend () -> T,
    crossinline transform: T.() -> R,
    dispatcher: CoroutineDispatcher
): Flow<Result<R>> = flow { emit(block().transform()) }.asResult().flowOn(dispatcher)

inline fun <T> ioResultFlow(
    crossinline block: suspend () -> T
): Flow<Result<T>> = resultFlow(block, Dispatchers.IO)

inline fun <T, R> ioResultFlow(
    crossinline block: suspend () -> T,
    crossinline transform: T.() -> R
): Flow<Result<R>> = resultFlow(block, transform, Dispatchers.IO)


