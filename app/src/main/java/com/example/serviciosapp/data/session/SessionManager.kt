package com.example.serviciosapp.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val SESSION_PREFS = "session_prefs"
private val Context.sessionDataStore by preferencesDataStore(name = SESSION_PREFS)

class SessionManager(private val context: Context) {
    companion object {
        private val KEY_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_ROLE = stringPreferencesKey("user_role")
    }

    val isLoggedInFlow: Flow<Boolean> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_LOGGED_IN] ?: false
    }

    val userIdFlow: Flow<String?> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_USER_ID]
    }

    val roleFlow: Flow<UserRole> = context.sessionDataStore.data.map { prefs ->
        val stored = prefs[KEY_ROLE]
        if (stored != null) {
            runCatching { UserRole.valueOf(stored) }.getOrDefault(UserRole.CONSUMER)
        } else {
            UserRole.CONSUMER
        }
    }

    suspend fun saveSession(userId: String, role: UserRole) {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = true
            prefs[KEY_USER_ID] = userId
            prefs[KEY_ROLE] = role.name
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = false
            prefs.remove(KEY_USER_ID)
            prefs.remove(KEY_ROLE)
        }
    }

    suspend fun currentUserId(): String? = context.sessionDataStore.data.first()[KEY_USER_ID]
}

enum class UserRole {
    CONSUMER,
    PROVIDER
}
