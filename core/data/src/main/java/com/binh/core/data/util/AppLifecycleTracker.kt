package com.binh.core.data.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLifecycleTracker @Inject constructor() : Application.ActivityLifecycleCallbacks {

    private var numStarted = 0
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        //
    }

    override fun onActivityStarted(activity: Activity) {
        if (numStarted == 0) {
            // app went to foreground
            Log.d(TAG, "app go to foreground")
        }
        numStarted++
    }

    override fun onActivityResumed(p0: Activity) {
        //
    }

    override fun onActivityPaused(p0: Activity) {
        //
    }

    override fun onActivityStopped(activity: Activity) {
        numStarted--
        if (numStarted == 0) {
            // app went to background
            Log.d(TAG, "app go to background")
        }
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        //
    }

    override fun onActivityDestroyed(p0: Activity) {
        //
    }

    val isForeground: Boolean
        get() = numStarted > 0

    companion object {
        const val TAG = "AppLifecycleTracker"
    }
}