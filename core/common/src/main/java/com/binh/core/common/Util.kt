package com.binh.core.common

import android.util.Log

inline fun tryCatch(crossinline call: () -> Any): Any? {
    return try {
        call()
    } catch (e: Exception) {
        Log.e("ERROR", "e", e)
        null
    }
}
