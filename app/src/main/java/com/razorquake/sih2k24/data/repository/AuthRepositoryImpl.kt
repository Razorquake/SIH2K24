package com.razorquake.sih2k24.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.razorquake.sih2k24.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
) : AuthRepository {
    private object PreferencesKeys {
        val IS_USER_AUTHENTICATED = booleanPreferencesKey("isUserAuthenticated")
    }

    override suspend fun setUserAuthenticated(isAuthenticated: Boolean) {
        datastore.edit {
            it[PreferencesKeys.IS_USER_AUTHENTICATED] = isAuthenticated
        }
    }

    override suspend fun isUserAuthenticated(): Flow<Boolean> {
        return datastore.data.map {
            it[PreferencesKeys.IS_USER_AUTHENTICATED] ?: false
        }
    }
}