package com.example.serviciosapp.data.favorites

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val FAVORITES_PREFS = "favorites_prefs"
private val Context.favoritesDataStore by preferencesDataStore(name = FAVORITES_PREFS)

class FavoritesRepository(private val context: Context) {
    companion object {
        private val KEY_FAVORITES = stringSetPreferencesKey("favorite_ids")
    }

    val favoritesFlow: Flow<Set<String>> = context.favoritesDataStore.data.map { prefs ->
        prefs[KEY_FAVORITES] ?: emptySet()
    }

    suspend fun toggleFavorite(id: String) {
        context.favoritesDataStore.edit { prefs ->
            val current = prefs[KEY_FAVORITES] ?: emptySet()
            prefs[KEY_FAVORITES] = if (current.contains(id)) current - id else current + id
        }
    }
}
