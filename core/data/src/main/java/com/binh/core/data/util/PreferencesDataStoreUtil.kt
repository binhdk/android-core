package com.binh.core.data.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


const val APP_PREFERENCES_NAME = "app_preferences"

val Context.preferencesSataStore: DataStore<Preferences> by preferencesDataStore(name = APP_PREFERENCES_NAME)