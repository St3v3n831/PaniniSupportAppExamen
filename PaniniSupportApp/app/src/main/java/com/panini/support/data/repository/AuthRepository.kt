package com.panini.support.data.repository

import com.google.gson.JsonParseException
import com.panini.support.core.ApiResult
import com.panini.support.core.UserMessages
import com.panini.support.data.remote.ApiService
import com.panini.support.data.remote.AuthInterceptorProvider
import com.panini.support.data.remote.model.LoginRequest
import com.panini.support.domain.model.User
import kotlinx.coroutines.delay

interface AuthRepository {
    suspend fun login(email: String, password: String): ApiResult<User>
    fun logout()
}

// ── Mock Auth (PoC) ─────────────────────────────────────────
class MockAuthRepository : AuthRepository {

    // Credenciales de demo
    private val validUsers = mapOf(
        "operaciones@panini.com" to "panini2026",
        "soporte@panini.com"     to "soporte123",
        "admin@panini.com"       to "admin2026"
    )

    override suspend fun login(email: String, password: String): ApiResult<User> {
        delay(1000) // Simula llamada de red
        return if (validUsers[email.lowercase()] == password) {
            ApiResult.Success(
                User(
                    id    = "USR-001",
                    name  = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = email,
                    role  = if (email.contains("admin")) "ADMIN" else "OPERATOR"
                )
            )
        } else {
            ApiResult.Error(
                message    = UserMessages.Auth.INVALID_CREDENTIALS,
                statusCode = 401
            )
        }
    }

    override fun logout() { /* no-op en mock */ }
}

// ── Remote Auth (para backend real) ─────────────────────────
class RemoteAuthRepository(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun login(email: String, password: String): ApiResult<User> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val body  = response.body()!!
                AuthInterceptorProvider.token = body.token
                val user  = body.user
                ApiResult.Success(User(user.id, user.name, user.email, user.role))
            } else {
                val msg = if (response.code() == 401)
                    UserMessages.Auth.INVALID_CREDENTIALS
                else
                    UserMessages.Auth.LOGIN_FAILED
                ApiResult.Error(message = msg, statusCode = response.code())
            }
        } catch (_: JsonParseException) {
            ApiResult.Error(message = UserMessages.Auth.LOGIN_JSON_MISMATCH)
        } catch (_: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }

    override fun logout() {
        AuthInterceptorProvider.token = null
    }
}
