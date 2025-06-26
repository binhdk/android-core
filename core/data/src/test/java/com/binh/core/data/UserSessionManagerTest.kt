package com.binh.core.data

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.binh.core.data.user.Token
import com.binh.core.data.user.User
import com.binh.core.data.user.UserSessionManager
import com.binh.core.testing.BaseTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserSessionManagerTest : BaseTest() {

    private lateinit var userSessionManager: UserSessionManager

    @Before
    fun setUp() {
        userSessionManager = UserSessionManager(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        userSessionManager.clearUserSession()
    }

    @Test
    fun startUserSessionAndGetSession() {
        // WHEN - save user session
        val user = User(
            1,
            "Test User",
            "test@gmail.com",
            "0912345678"
        )
        val token = Token("accessToken", "refreshToken")
        userSessionManager.startUserSession(user, token)

        // WHEN - load session from preference
        val userId = userSessionManager.getId()
        val accessToken = userSessionManager.getAccessToken()
        val refreshToken = userSessionManager.getRefreshToken()

        // THEN - loaded data match expected values
        assertThat(userId, `is`(user.id))
        assertThat(accessToken, `is`(token.accessToken))
        assertThat(refreshToken, `is`(token.refreshToken))

    }

    @Test
    fun updateTokenAndGetToken() {
        // GIVEN - user session stored
        val user = User(
            1,
            "Test User",
            "test@gmail.com",
            "0912345678"
        )
        val token = Token("accessToken", "refreshToken")
        userSessionManager.startUserSession(user, token)

        // WHEN - update token
        val updatedToken = Token(" updated accessToken", " updated refreshToken")
        userSessionManager.updateToken(updatedToken)

        val accessToken = userSessionManager.getAccessToken()
        val refreshToken = userSessionManager.getRefreshToken()

        // THEN - loaded data match expected values
        assertThat(accessToken, `is`(updatedToken.accessToken))
        assertThat(refreshToken, `is`(updatedToken.refreshToken))
    }


    @Test
    fun clearUserSessionAndGetSession() {
        // GIVEN - user session stored
        val user = User(
            1,
            "Test User",
            "test@gmail.com",
            "0912345678"
        )
        val token = Token("accessToken", "refreshToken")
        userSessionManager.startUserSession(user, token)

        // WHEN - clear user session
        userSessionManager.clearUserSession()

        // THEN - session cleared
        val haveSession = userSessionManager.haveStoredToken()
        assertThat(haveSession, `is`(false))
    }
}