package com.binh.core.ui.ext

import android.content.res.Resources
import android.util.Log


fun Resources.getStringOrNull(restId: Int): String? {
    return try {
        getString(restId)
    } catch (e: Exception) {
        Log.e("Resources.getStringOrNull", "e", e)
        null
    }
}