// src/main/kotlin/com/example/soundstore/dto/AuthResponses.kt
package com.example.soundstore.dto

/**
 * Ответ после успешной регистрации или логина:
 * возвращает JWT-токен и email пользователя.
 */
data class AuthResponse(
    val token: String,
    val email: String
)
