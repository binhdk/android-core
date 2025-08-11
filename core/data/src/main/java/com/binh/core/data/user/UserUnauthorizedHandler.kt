package com.binh.core.data.user

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUnauthorizedHandler @Inject constructor(
    private val userSessionManager: UserSessionManager
) {
    suspend fun handleUnAuthorized() {
        userSessionManager.clearUserSession()
        Log.d(TAG, "user session cleared")
    }

    companion object {
        const val TAG = "UserUnauthorizedHandler"
    }
}