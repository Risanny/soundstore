// src/main/kotlin/com/example/soundstore/dto/AuthRequests.kt
package com.example.soundstore.dto

/**
 * Тело запроса для регистрации нового пользователя.
 * Содержит email и пароль.
 */
data class RegisterRequest(
    val email: String,
    val password: String
)

/**
 * Тело запроса для входа.
 */
data class LoginRequest(
    val email: String,
    val password: String
)
