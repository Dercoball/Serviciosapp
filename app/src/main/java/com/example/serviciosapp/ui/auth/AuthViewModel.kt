package com.example.serviciosapp.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviciosapp.data.auth.FakeAuthRepository
import com.example.serviciosapp.data.session.SessionManager
import com.example.serviciosapp.data.session.UserRole
import com.example.serviciosapp.ui.state.UiState
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: FakeAuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var identifier by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var uiState by mutableStateOf<UiState<String>>(UiState.Idle)
        private set

    fun onIdentifierChange(value: String) {
        identifier = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    fun login(onSuccess: (String) -> Unit) {
        if (!isInputValid()) {
            uiState = UiState.Error("Datos invalidos (contrasena minimo 4 caracteres).")
            return
        }
        viewModelScope.launch {
            uiState = UiState.Loading
            val result = authRepository.login(identifier.trim(), password)
            uiState = result.fold(
                onSuccess = {
                    onSuccess(it)
                    UiState.Success(it)
                },
                onFailure = { UiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun register(onSuccess: (String) -> Unit) {
        if (!isInputValid()) {
            uiState = UiState.Error("Datos invalidos (contrasena minimo 4 caracteres).")
            return
        }
        viewModelScope.launch {
            uiState = UiState.Loading
            val result = authRepository.register(identifier.trim(), password)
            uiState = result.fold(
                onSuccess = {
                    onSuccess(it)
                    UiState.Success(it)
                },
                onFailure = { UiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun demoLogin(role: UserRole, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            uiState = UiState.Loading
            val userId = "demo-${role.name.lowercase()}"
            sessionManager.saveSession(userId, role)
            uiState = UiState.Success(userId)
            onSuccess(userId)
        }
    }

    private fun isInputValid(): Boolean =
        identifier.isNotBlank() && password.length >= 4
}
