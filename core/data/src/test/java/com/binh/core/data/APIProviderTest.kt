package com.binh.core.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.binh.core.data.network.APIProvider
import com.binh.core.data.user.network.UserAPI
import com.binh.core.data.user.UserSessionManager
import com.binh.core.data.util.isNetworkAvailable
import com.binh.core.testing.BaseTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class APIProviderTest : BaseTest() {


    private lateinit var apiProvider: APIProvider


    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val userSessionManager = UserSessionManager(context)
        apiProvider = APIProvider(
            userSessionManager::getAccessToken,
            userSessionManager::getRefreshToken,
            { newAccessToken: String, refreshToken: String ->
                //
            },
            context::isNetworkAvailable
        )


    }

    @After
    fun tearDown() {
    }

    @Test
    fun createService() {
        // WHEN - create userAPI
        val userAPI = apiProvider.createService<UserAPI>()

        // THEN - api service created
        assert(true)
    }
}