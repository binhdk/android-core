package com.binh.core.ui

import com.binh.core.data.util.Failure
import com.binh.core.data.util.asFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Failure(val failure: com.binh.core.data.util.Failure) : UiState<Nothing>()
    object Loading : UiState<Nothing>()

    object None : UiState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failure -> "Failure[error=$failure]"
            is Loading -> "Loading"
            None -> "None"
        }
    }

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

    fun errorOrNull(): com.binh.core.data.util.Failure? =
        when (this) {
            is Failure -> failure
            else -> null
        }
}

val <T> T.asSuccessUiState get() = UiState.Success(this)
val Failure.asFailureUiState get() = UiState.Failure(this)
val Throwable.asFailureUiState get() = UiState.Failure(this.asFailure)

fun <T> UiState<T>.successOr(fallback: T): T {
    Result
    return (this as? UiState.Success<T>)?.data ?: fallback
}


inline fun <T> UiState<T>.onFailure(action: (failure: Failure) -> Unit): UiState<T> {
    if (this is UiState.Failure) action(this.failure)
    return this
}


inline fun <T> UiState<T>.onSuccess(action: (value: T) -> Unit): UiState<T> {
    if (this is UiState.Success) action(data)
    return this
}

inline fun <T> UiState<T>.onLoading(action: () -> Unit): UiState<T> {
    if (this is UiState.Loading) action()
    return this
}

inline fun <T> UiState<T>.fold(
    onSuccess: (value: T) -> Unit,
    onFailure: (failure: Failure) -> Unit,
) {
    when (this) {
        is UiState.Success -> onSuccess(data)
        is UiState.Failure -> onFailure(failure)
        else -> {}
    }
}

fun <T> Flow<T>.asUiState(): Flow<UiState<T>> {
    return this
        .map<T, UiState<T>> {
            UiState.Success(it)
        }
        .onStart { emit(UiState.Loading) }
        .catch { emit(UiState.Failure(it.asFailure)) }
}

fun <T> MutableStateFlow<UiState<T>>.setSuccess(value: T) {
    this.value = UiState.Success(value)
}

fun <T> MutableStateFlow<UiState<T>>.setLoading() {
    this.value = UiState.Loading
}

fun <T> MutableStateFlow<UiState<T>>.setError(throwable: Throwable?) {
    this.value = UiState.Failure(throwable.asFailure)
}

fun <T> MutableStateFlow<UiState<T>>.setError(failure: Failure) {
    this.value = UiState.Failure(failure)
}
