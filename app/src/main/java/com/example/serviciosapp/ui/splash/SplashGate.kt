package com.example.serviciosapp.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.serviciosapp.data.session.SessionManager
import kotlinx.coroutines.flow.first

@Composable
fun SplashGate(
    sessionManager: SessionManager,
    onAuthRequired: () -> Unit,
    onSessionActive: () -> Unit
) {
    LaunchedEffect(Unit) {
        val isLogged = sessionManager.isLoggedInFlow.first()
        if (isLogged) onSessionActive() else onAuthRequired()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(text = "Cargando sesion...")
    }
}
