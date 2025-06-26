package com.binh.core.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.binh.core.data.network.APIProvider
import com.binh.core.data.user.network.LoginRequest
import com.binh.core.data.user.network.UserAPI
import com.binh.core.data.user.UserSessionManager
import com.binh.core.data.user.network.asExternalModel
import com.binh.core.data.util.isNetworkAvailable
import com.binh.core.testing.BaseTest
import com.binh.core.testing.MainCoroutineRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserAPITest : BaseTest() {

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiProvider: APIProvider

    private lateinit var userAPI: UserAPI

    private lateinit var userSessionManager: UserSessionManager

    @Before
    fun setup() {
        val context = getApplicationContext<Context>()
        userSessionManager = UserSessionManager(context)

        apiProvider = APIProvider(
            userSessionManager::getAccessToken,
            userSessionManager::getRefreshToken,
            { newAccessToken: String, refreshToken: String ->
                //
            },
            context::isNetworkAvailable
        )

        userAPI = apiProvider.createService()

    }

    @After
    fun tearDown() {
        userSessionManager.clearUserSession()
    }

    @Test
    fun login() = runTest {
        // WHEN - login
        val loginRequest = LoginRequest("binh@gmail.com", "testpassword")
        val loginResponse = userAPI.login(loginRequest)

        // THEN - The response data contains the expected values
        assertThat(loginResponse.data.user.email, `is`("binh@gmail.com"))
    }

    @Test
    fun getUserProfile() = runTest {
        // GIVEN - user logged in
        val loginRequest = LoginRequest("binh@gmail.com", "testpassword")
        val loginResponse = userAPI.login(loginRequest)

        // GIVEN - session stored
        userSessionManager.startUserSession(
            loginResponse.data.user.asExternalModel,
            loginResponse.data.token.asExternalModel
        )

        // THEN - The response data contains the expected values
        assertThat(loginResponse.data.user.email, `is`("binh@gmail.com"))

        // WHEN - get user from server
        val remoteUser = userAPI.getCurrentUserProfile().data

        // THEN - response data contains expected values
        assertThat(remoteUser.email, `is`("binh@gmail.com"))
    }

    @Test
    fun logout() = runTest {
        // GIVEN - user logged in
        val loginRequest = LoginRequest("binh@gmail.com", "testpassword")
        val loginResponse = userAPI.login(loginRequest)

        // GIVEN - session stored
        userSessionManager.startUserSession(
            loginResponse.data.user.asExternalModel,
            loginResponse.data.token.asExternalModel
        )

        // THEN - The response data contains the expected values
        assertThat(loginResponse.data.user.email, `is`("binh@gmail.com"))

        // WHEN - logout user
        userAPI.logout()

        // THEN - user successfully logout
        assert(true)
    }


}