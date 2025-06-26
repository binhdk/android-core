package com.binh.core.data.user

import android.util.Log
import com.binh.core.data.Result
import com.binh.core.data.ioResult
import com.binh.core.data.user.network.LoginRequest
import com.binh.core.data.user.network.LoginResponse
import com.binh.core.data.user.network.UserAPI
import com.binh.core.data.user.network.asExternalModel
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userAPI: UserAPI,
    private val userSessionManager: UserSessionManager,
) {
    suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponse> {
        return ioResult {
            val networkLoginResponse = userAPI.login(LoginRequest(email, password)).data
            val loginResponse = LoginResponse(
                networkLoginResponse.user.asExternalModel,
                networkLoginResponse.token.asExternalModel
            )
            userSessionManager.startUserSession(
                loginResponse.user,
                loginResponse.token
            )
            loginResponse
        }
    }

    suspend fun getCurrentUser(): Result<User> {
        return ioResult {
            val networkUser = userAPI.getCurrentUserProfile().data
            val user = networkUser.asExternalModel
            userSessionManager.updateUser(user)
            user
        }
    }

    suspend fun logout(): Boolean {
        try {
            userAPI.logout()
        } catch (e: Exception) {
            Log.e(TAG, "e", e)
        } finally {
            userSessionManager.clearUserSession()
        }
        return true
    }

    companion object {
        const val TAG = "UserRepository"
    }
}