package com.binh.core.common.exts

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> MutableSharedFlow<T>.emitAndResetReplayCache(value: T) {
    emit(value)
    resetReplayCache()
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> MutableSharedFlow<T>.tryEmitAndResetReplayCache(value: T) {
    tryEmit(value)
    resetReplayCache()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Flow<T>.collectDistinctUntilChanged(collector: FlowCollector<T>) {
    distinctUntilChanged().collect(collector)
}

inline fun <T> flowOfBlock(
    crossinline block: suspend () -> T,
): Flow<T> = flow { emit(block()) }
