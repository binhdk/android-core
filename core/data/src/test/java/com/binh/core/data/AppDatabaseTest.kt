package com.binh.core.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.binh.core.data.local.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        appDatabase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun createDao() {
        // WHEN - create userDao
        val userDao = appDatabase.userDao()

        // THEN - Dao created success
        assert(true)
    }
}