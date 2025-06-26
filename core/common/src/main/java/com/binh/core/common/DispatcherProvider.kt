package com.binh.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

interface BaseDispatcherProvider {
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val singleIO: CoroutineDispatcher
    val main: CoroutineDispatcher
    val mainImmediate: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

@OptIn(ExperimentalCoroutinesApi::class)
class DispatcherProvider @Inject constructor() : BaseDispatcherProvider {
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val singleIO: CoroutineDispatcher = Dispatchers.IO.limitedParallelism(1)
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val mainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}