package com.binh.core.ui.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow


/**
 * Used as a wrapper for data that is exposed via a Flow/Channel that represents an event.
 */
class Event<out T>(private var content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun getContent(): T {
        return this.content
    }
}


suspend fun <T> Flow<Event<T>>.collectEvent(action: (T) -> Unit) {
    collect { it.getContentIfNotHandled()?.let { action(it) } }
}

suspend fun <T> FlowCollector<Event<T>>.emitEvent(value: T) {
    emit(Event(value))
}

fun <T> MutableStateFlow<Event<T>>.setEvent(value: T) {
    this.value = Event(value)
}

suspend fun <T> Channel<Event<T>>.sendEvent(value: T) {
    send(Event(value))
}

fun <T> Channel<Event<T>>.trySendEvent(value: T) {
    trySend(Event(value))
}

fun <T> Channel<Event<T>>.sendBlockingEvent(value: T) {
    trySendBlocking(Event(value))
}
