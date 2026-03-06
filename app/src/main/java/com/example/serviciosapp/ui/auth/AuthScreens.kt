package com.example.serviciosapp.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.serviciosapp.ui.state.UiState

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    onDemoConsumer: () -> Unit,
    onDemoProvider: () -> Unit
) {
    AuthScreen(
        title = "Iniciar sesion",
        primaryButtonLabel = "Entrar",
        secondaryButtonLabel = "Crear cuenta",
        onPrimary = { viewModel.login(onLoginSuccess) },
        onSecondary = onNavigateToRegister,
        onGoogle = {
            viewModel.onIdentifierChange("google.user@fake.com")
            viewModel.onPasswordChange("google")
            viewModel.login(onLoginSuccess)
        },
        extraButtons = {
            DemoButtons(onDemoConsumer = onDemoConsumer, onDemoProvider = onDemoProvider)
        },
        viewModel = viewModel
    )
}

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: (String) -> Unit
) {
    AuthScreen(
        title = "Crear cuenta",
        primaryButtonLabel = "Registrarme",
        secondaryButtonLabel = "Ya tengo cuenta",
        onPrimary = { viewModel.register(onRegisterSuccess) },
        onSecondary = onNavigateToLogin,
        onGoogle = {
            viewModel.onIdentifierChange("google.user@fake.com")
            viewModel.onPasswordChange("google")
            viewModel.register(onRegisterSuccess)
        },
        viewModel = viewModel
    )
}

@Composable
private fun AuthScreen(
    title: String,
    primaryButtonLabel: String,
    secondaryButtonLabel: String,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit,
    onGoogle: () -> Unit,
    extraButtons: @Composable (() -> Unit)? = null,
    viewModel: AuthViewModel
) {
    val uiState = viewModel.uiState

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = viewModel.identifier,
                onValueChange = viewModel::onIdentifierChange,
                label = { Text("Email o telefono") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contrasena") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState is UiState.Error) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = onPrimary,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is UiState.Loading
            ) {
                Text(primaryButtonLabel)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onGoogle,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is UiState.Loading
            ) {
                Text("Continuar con Google (simulado)")
            }
            Spacer(modifier = Modifier.height(8.dp))
            extraButtons?.invoke()
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onSecondary) {
                Text(secondaryButtonLabel)
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (uiState is UiState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LaunchedEffect(uiState) {
                if (uiState is UiState.Success) {
                    viewModel.onPasswordChange("")
                }
            }
        }
    }
}

@Composable
private fun DemoButtons(
    onDemoConsumer: () -> Unit,
    onDemoProvider: () -> Unit
) {
    Button(
        onClick = onDemoConsumer,
        modifier = Modifier.fillMaxWidth()
    ) { Text("Entrar como Consumidor (Demo)") }
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = onDemoProvider,
        modifier = Modifier.fillMaxWidth()
    ) { Text("Entrar como Prestador (Demo)") }
}
