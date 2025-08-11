package com.binh.core.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.binh.core.data.local.AppDatabase
import com.binh.core.data.user.local.UserEntity
import com.binh.core.testing.MainCoroutineRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var database: AppDatabase

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertUserAndGetById() = runTest {
        // WHEN - insert
        val user = UserEntity(
            1,
            "Test User",
            "test@gmail.com",
            "0912345678",
            "accessToken",
            "refreshToken"
        )
        database.userDao().add(user)

        // WHEN - Get user by id from the database
        val loaded = database.userDao().getUserById(user.id)

        // THEN - The loaded data contains the expected values
        assertThat(loaded as UserEntity, notNullValue())
        assertThat(loaded.id, `is`(user.id))
        assertThat(loaded.email, `is`(user.email))
        assertThat(loaded.accessToken, `is`(user.accessToken))
        assertThat(loaded.refreshToken, `is`(user.refreshToken))

    }


    @Test
    fun insertUserAndGetUsers() = runTest {
        // WHEN - insert
        val user = UserEntity(
            1,
            "Test User",
            "test@gmail.com",
            "0912345678",
            "accessToken",
            "refreshToken"
        )
        database.userDao().add(user)

        // WHEN - Get users from the database
        val users = database.userDao().getAll()

        // THEN - The loaded data contains the expected values
        assertThat(users[0], notNullValue())
        assertThat(users[0].id, `is`(user.id))
        assertThat(users[0].email, `is`(user.email))
        assertThat(users[0].accessToken, `is`(user.accessToken))
        assertThat(users[0].refreshToken, `is`(user.refreshToken))

    }

    @Test
    fun updateUserAndGetById() = runTest {
        // GIVEN - a user inserted
        val originalUser = UserEntity(
            1,
            "Test User",
            "test@gmail.com",
            "0912345678",
            "accessToken",
            "refreshToken"
        )
        database.userDao().add(originalUser)

        // WHEN - user is updated
        val updatedUser = UserEntity(
            originalUser.id,
            "Updated Test User",
            "Updated test@gmail.com",
            "0987654321",
            "Updated accessToken",
            "Updated refreshToken"
        )
        database.userDao().update(updatedUser)

        // WHEN - get user from database
        val loaded = database.userDao().getUserById(originalUser.id)

        // THEN - The loaded data contains the expected values
        assertThat(loaded as UserEntity, notNullValue())
        assertThat(loaded.id, `is`(originalUser.id))
        assertThat(loaded.email, `is`(updatedUser.email))
        assertThat(loaded.accessToken, `is`(updatedUser.accessToken))
        assertThat(loaded.refreshToken, `is`(updatedUser.refreshToken))

    }

    @Test
    fun deleteUserAndGetUsers() = runTest {
        // GIVEN - a user inserted
        val user = UserEntity(
            1,
            "Test User",
            "test@gmail.com",
            "0912345678",
            "accessToken",
            "refreshToken"
        )
        database.userDao().add(user)

        // WHEN - delete a user by id
        database.userDao().deleteById(user.id)

        // THEN - The list is empty
        val users = database.userDao().getAll()
        assertThat(users.isEmpty(), `is`(true))

    }
}