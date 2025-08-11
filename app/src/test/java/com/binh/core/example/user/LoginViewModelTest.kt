package com.binh.core.example.user

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.binh.core.data.network.APIProvider
import com.binh.core.data.user.RefreshedTokenCallback
import com.binh.core.data.user.UserRepository
import com.binh.core.data.user.UserSessionManager
import com.binh.core.data.util.isNetworkAvailable
import com.binh.core.example.ui.user.LoginViewModel
import com.binh.core.testing.BaseTest
import com.binh.core.testing.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class LoginViewModelTest : BaseTest() {

    private lateinit var loginViewModel: LoginViewModel


    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()


    @Before
    fun setUp() {
        val applicationContext: Context = ApplicationProvider.getApplicationContext()

        val userSessionManager = UserSessionManager(applicationContext)

        val refreshedTokenCallback = RefreshedTokenCallback(userSessionManager)

        val apiClient = APIProvider(
            userSessionManager::getAccessToken,
            userSessionManager::getRefreshToken,
            refreshedTokenCallback::onTokenRefreshed,
            applicationContext::isNetworkAvailable
        )

        val userRepository = UserRepository(
            apiClient.createService(),
            userSessionManager
        )

        loginViewModel = LoginViewModel(userRepository)
    }

    @After
    fun tearDown() {
        //
    }

    @Test
    fun testLogin() = runTest {
        // WHEN - login
        loginViewModel.login("admin@gmail.com", "admin1234!")

        // THEN - response data contains expected values
        assertThat(loginViewModel.loginEvent.value.getContentIfNotHandled(), notNullValue())
        assertThat(
            loginViewModel.loginEvent.value.getContentIfNotHandled()?.getOrNull()?.email,
            `is`("admin@gmail.com")
        )

    }
}
