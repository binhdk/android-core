package com.binh.core.example.ui.user

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.binh.core.data.user.UserRepository
import com.binh.core.example.R
import com.binh.core.example.ui.user.LoginFragment
import com.binh.core.uitesthiltmanifest.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@MediumTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: UserRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testLaunch() {

        // WHEN - Login fragment launched to perform user login
        launchFragmentInHiltContainer<LoginFragment>(null, R.style.Theme_App)

        // THEN - Login fragment is displayed on the screen
        // make sure ui components are displayed correctly
        onView(withId(R.id.username)).check(matches(isDisplayed()))
    }
}