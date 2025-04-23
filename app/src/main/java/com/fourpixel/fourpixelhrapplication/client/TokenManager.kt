package com.fourpixel.fourpixelhrapplication.client

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class TokenManager(private val context: Context) {

    private val tokenKey = stringPreferencesKey("bearer_token")

    // Function to save the token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    // Function to retrieve the token
    val tokenFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[tokenKey]
        }
}
