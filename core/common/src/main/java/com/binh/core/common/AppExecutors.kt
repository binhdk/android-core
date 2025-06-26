package com.binh.core.common

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


const val THREAD_COUNT = 3

open class AppExecutors constructor(
    val diskIO: Executor = DiskIOThreadExecutor(),
    val networkIO: Executor = NetworkIOThreadExecutor(),
    val mainThread: Executor = MainThreadExecutor()
)

class MainThreadExecutor : Executor {
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
        mainThreadHandler.post(command)
    }
}

/**
 * Executor that runs a task on a new background thread.
 */
class DiskIOThreadExecutor : Executor {
    private val mDiskIO: Executor = Executors.newSingleThreadExecutor()
    override fun execute(command: Runnable) {
        mDiskIO.execute(command)
    }
}

/**
 * Executor that runs a task on a fixed thread pool.
 */
class NetworkIOThreadExecutor : Executor {
    private val mDiskIO: Executor = Executors.newFixedThreadPool(THREAD_COUNT)
    override fun execute(command: Runnable) {
        mDiskIO.execute(command)
    }
}
