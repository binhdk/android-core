package com.binh.core.data.user

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.binh.core.data.util.jsonToObject
import com.binh.core.data.util.toJson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@SuppressLint("ApplySharedPref")
@Singleton
class UserSessionManager @Inject constructor(@ApplicationContext context: Context) {
    companion object {
        const val TAG = "UserSessionManager"
        const val USER_SESSION_PREF_NAME = "user_session_preferences"
        const val PREF_KEY_USER_ID = "user_id"
        const val PREF_KEY_ACCESS_TOKEN = "access_token"
        const val PREF_KEY_REFRESH_TOKEN = "refresh_token"
        const val PREF_KEY_USER = "user"
        const val PREF_KEY_TOKEN = "token"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private val prefs = EncryptedSharedPreferences.create(
        context,
        USER_SESSION_PREF_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @get:Synchronized
    val user: User?
        get() = getSavedUser()

    @get:Synchronized
    val token: Token?
        get() = getSavedToken()

    @Synchronized
    fun startUserSession(user: User, token: Token) {
        saveUserPref(user, token)
    }

    private fun getSavedUser(): User? {
        return prefs.getString(PREF_KEY_USER, null)?.jsonToObject()
    }

    private fun getSavedToken(): Token? {
        return prefs.getString(PREF_KEY_TOKEN, null)?.jsonToObject()
    }

    @Synchronized
    fun updateUser(user: User) {
        prefs.edit(commit = true) {
            putString(
                PREF_KEY_USER,
                user.toJson()
            )
        }

    }

    @Synchronized
    private fun saveUserPref(user: User, token: Token) {
        prefs.edit(commit = true) {
            putString(PREF_KEY_ACCESS_TOKEN, token.accessToken)
                .putString(PREF_KEY_REFRESH_TOKEN, token.refreshToken)
                .putLong(PREF_KEY_USER_ID, user.id)
                .putString(PREF_KEY_USER, user.toJson())
                .putString(PREF_KEY_TOKEN, token.toJson())
        }
    }


    @Synchronized
    fun getAccessToken(): String {
        return prefs.getString(PREF_KEY_ACCESS_TOKEN, null) ?: ""
    }

    @Synchronized
    fun getRefreshToken(): String {
        return prefs.getString(PREF_KEY_REFRESH_TOKEN, null) ?: ""
    }

    @Synchronized
    fun haveStoredToken(): Boolean {
        val storedAccessToken =
            prefs.getString(PREF_KEY_ACCESS_TOKEN, null)
        val storedRefreshToken =
            prefs.getString(PREF_KEY_REFRESH_TOKEN, null)
        return !(storedAccessToken.isNullOrBlank() || storedRefreshToken.isNullOrBlank())
    }


    @Synchronized
    fun getId(): Long {
        return prefs.getLong(PREF_KEY_USER_ID, -1)
    }


    @Synchronized
    fun clearUserSession() {
        prefs.edit(commit = true) {
            clear()
        }
    }

    @Synchronized
    fun updateToken(token: Token) {
        prefs.edit(commit = true) {
            putString(PREF_KEY_ACCESS_TOKEN, token.accessToken)
                .putString(PREF_KEY_REFRESH_TOKEN, token.refreshToken)
                .putString(
                    PREF_KEY_TOKEN,
                    token.toJson()
                )
        }
    }


}


typealias AccessTokenProvider = () -> String

typealias RefreshTokenProvider = () -> String

typealias OnTokenRefreshed = (newAccessToken: String, refreshToken: String) -> Unit

@Singleton
class RefreshedTokenCallback @Inject constructor(
    private val userSessionManager: UserSessionManager
) {
    @Synchronized
    fun onTokenRefreshed(
        newAccessToken: String,
        refreshToken: String
    ) {
        //check if token refresh for current user
        if (refreshToken == userSessionManager.getRefreshToken()) {
            //store new access token for current user
            userSessionManager.updateToken(Token(newAccessToken, refreshToken))
            Log.d(TAG, "new access token updated")
        }
    }

    companion object {
        const val TAG = "RefreshTokenCallback"
    }
}