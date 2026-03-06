package com.example.serviciosapp.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.serviciosapp.data.session.SessionManager
import com.example.serviciosapp.data.session.UserRole
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    sessionManager: SessionManager,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val userId by sessionManager.userIdFlow.collectAsState(initial = "Invitado")
    val role by sessionManager.roleFlow.collectAsState(initial = UserRole.CONSUMER)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Usuario", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(userId ?: "Invitado", style = MaterialTheme.typography.bodyLarge)
            Text("Rol: ${role.name}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.weight(1f, fill = true))

            Button(
                onClick = {
                    scope.launch {
                        sessionManager.clearSession()
                        onLogout()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesion")
            }
        }
    }
}
