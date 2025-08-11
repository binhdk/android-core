package com.binh.core.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.binh.core.data.network.APIProvider
import com.binh.core.data.user.RefreshedTokenCallback
import com.binh.core.data.user.UserRepository
import com.binh.core.data.user.UserSessionManager
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
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class UserRepositoryTest : BaseTest() {

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiProvider: APIProvider

    private lateinit var userSessionManager: UserSessionManager

    private lateinit var refreshedTokenCallback: RefreshedTokenCallback

    private lateinit var userRepository: UserRepository


    @Before
    fun setUp() {
        val applicationContext: Context = ApplicationProvider.getApplicationContext()

        userSessionManager = UserSessionManager(applicationContext)

        refreshedTokenCallback = RefreshedTokenCallback(userSessionManager)

        apiProvider = APIProvider(
            userSessionManager::getAccessToken,
            userSessionManager::getRefreshToken,
            refreshedTokenCallback::onTokenRefreshed,
            applicationContext::isNetworkAvailable
        )

        userRepository = UserRepository(
            apiProvider.createService(),
            userSessionManager
        )
    }

    @After
    fun tearDown() {
        userSessionManager.clearUserSession()
    }

    @Test
    fun login() = runTest {
        // GIVEN - user login result
        val loginResponse =
            userRepository.login("binh@gmail.com", "testpassword").getOrNull()!!

        // THEN - The loaded data contains the expected values
        assertThat(loginResponse.user.email, `is`("binh@gmail.com"))
    }

    @Test
    fun getCurrentUser() = runTest {
        // GIVEN - user logged in
        val loginResponse = userRepository.login("binh@gmail.com", "testpassword").getOrNull()!!

        // GIVEN - user session stored
        userSessionManager.startUserSession(loginResponse.user, loginResponse.token)

        // THEN - The loaded data contains the expected values
        assertThat(loginResponse.user.email, `is`("binh@gmail.com"))

    }

    @Test
    fun logout() = runTest {
        // GIVEN - user logged in
        val loginResponse = userRepository.login("binh@gmail.com", "testpassword").getOrNull()!!

        // GIVEN - user session stored
        userSessionManager.startUserSession(loginResponse.user, loginResponse.token)

        // WHEN - logout user
        userRepository.logout()

        // THEN - The list is empty
        val user = userSessionManager.user
        assertEquals(user, null)
    }
}