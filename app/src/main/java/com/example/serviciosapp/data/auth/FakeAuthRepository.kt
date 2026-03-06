package com.example.serviciosapp.data.auth

import com.example.serviciosapp.data.session.SessionManager
import com.example.serviciosapp.data.session.UserRole
import java.util.UUID
import kotlinx.coroutines.delay

class FakeAuthRepository(private val sessionManager: SessionManager) {

    suspend fun login(identifier: String, password: String): Result<String> {
        delay(600)
        if (password.length < 4) return Result.failure(IllegalArgumentException("Contraseña muy corta"))
        val userId = generateUserId(identifier)
        sessionManager.saveSession(userId, UserRole.CONSUMER)
        return Result.success(userId)
    }

    suspend fun register(identifier: String, password: String): Result<String> {
        delay(600)
        if (password.length < 4) return Result.failure(IllegalArgumentException("Contraseña muy corta"))
        val userId = generateUserId(identifier)
        sessionManager.saveSession(userId, UserRole.CONSUMER)
        return Result.success(userId)
    }

    private fun generateUserId(identifier: String): String {
        return "user-${identifier.hashCode().toUInt()}-${UUID.randomUUID().toString().take(8)}"
    }
}
