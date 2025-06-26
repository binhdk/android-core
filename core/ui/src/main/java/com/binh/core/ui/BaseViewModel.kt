package com.binh.core.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binh.core.data.Result
import com.binh.core.data.catchFailure
import com.binh.core.data.network.RefreshTokenUnAuthorizedException
import com.binh.core.data.user.UserUnauthorizedHandler
import com.binh.core.data.util.Failure
import com.binh.core.data.util.asFailure
import com.binh.core.ui.util.Event
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseViewModel : ViewModel() {

    @Inject
    lateinit var userUnAuthorizedHandler: UserUnauthorizedHandler

    private val _failureEvent = MutableStateFlow<Event<Failure>?>(null)
    val failureEvent get() = _failureEvent.asStateFlow()

    private val _loadingEvent = MutableStateFlow<Event<Boolean>?>(null)
    val loadingEvent get() = _loadingEvent.asStateFlow()


    /**
     * Launch a coroutine with ViewModel Scope
     */
    inline fun launch(
        crossinline onFailure: (failure: Failure) -> Unit = { failure: Failure ->
            noticeFailure(failure)
        },
        crossinline block: suspend CoroutineScope.() -> Unit,
    ) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "uncaught", throwable)
            loaded()
            onFailure.invoke(throwable.asFailure)
        }) {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                Log.e(TAG, "caught", e)
                loaded()
                handleIfUnauthorized(e)
                onFailure.invoke(e.asFailure)
            }
        }
    }

    suspend fun handleFailure(failure: Failure) {
        failure.throwable?.let { handleIfUnauthorized(it) }
        noticeFailure(failure)
    }

    fun noticeFailure(failure: Failure) {
        _failureEvent.value = Event(failure)
    }

    fun loading() {
        _loadingEvent.value = Event(true)
    }

    fun loaded() {
        _loadingEvent.value = Event(false)
    }

    suspend fun handleIfUnauthorized(throwable: Throwable) {
        // check unauthorized
        if (throwable is RefreshTokenUnAuthorizedException)
            userUnAuthorizedHandler.handleUnAuthorized()
    }

    suspend fun <T> withLoading(block: suspend () -> T): T {
        try {
            loading()
            val data = block.invoke()
            loaded()
            return data
        } catch (e: Exception) {
            Log.e(TAG, "caught", e)
            loaded()
            throw e
        }
    }

    protected fun <T> Flow<Result<T>>.withLoading(handleError: Boolean = true): Flow<Result<T>> {
        return onEach {
            when (it) {
                is Result.Loading -> loading()
                is Result.Success -> loaded()
                is Result.Failure -> {
                    loaded()
                    it.exception?.let { throwable ->
                        Log.e(TAG, "caught", throwable)
                        handleIfUnauthorized(throwable)

                    }
                    if (handleError)
                        noticeFailure(it.exception.asFailure)
                }
            }
        }
    }

    protected suspend fun <T> Flow<Result<T>>.handleFailure(): Flow<Result<T>> {
        return catchFailure { handleFailure(it) }
    }

    protected suspend fun <T> Result<T>.handleFailure(): Result<T> {
        if (this is Result.Failure) {
            handleFailure(exception.asFailure)
        }
        return this
    }

    protected suspend fun <T> Result<T>.handleUnAuthorizedFailure(): Result<T> {
        exceptionOrNull()?.let { handleIfUnauthorized(it) }
        return this
    }

    companion object {
        const val TAG = "BaseViewModel"
    }
}