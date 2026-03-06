package com.example.serviciosapp

import android.content.Context
import com.example.serviciosapp.data.auth.FakeAuthRepository
import com.example.serviciosapp.data.favorites.FavoritesRepository
import com.example.serviciosapp.data.sample.SampleData
import com.example.serviciosapp.data.session.SessionManager

/**
 * Manual DI container to keep dependencies in one place (no Hilt for now).
 */
class AppContainer(context: Context) {
    val sessionManager: SessionManager by lazy { SessionManager(context) }
    val authRepository: FakeAuthRepository by lazy { FakeAuthRepository(sessionManager) }
    val sampleData: SampleData = SampleData
    val favoritesRepository: FavoritesRepository by lazy { FavoritesRepository(context) }
}
