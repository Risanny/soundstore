package com.example.soundstore.controller

import com.example.soundstore.dto.AuthResponse
import com.example.soundstore.dto.LoginRequest
import com.example.soundstore.dto.RegisterRequest
import com.example.soundstore.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST-контроллер для аутентификации.
 * Открывает два эндпоинта: регистрация и логин.
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    /**
     * Регистрация нового пользователя.
     * Принимает JSON { email, password } и возвращает JWT в ответе.
     *
     * Пример запроса:
     * POST /api/auth/register
     * {
     *   "email": "user@example.com",
     *   "password": "secret"
     * }
     */
    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): ResponseEntity<AuthResponse> {
        val response = authService.register(req)
        return ResponseEntity.ok(response)
    }

    /**
     * Вход в систему.
     * Принимает JSON { email, password } и возвращает JWT.
     *
     * Пример запроса:
     * POST /api/auth/login
     * {
     *   "email": "user@example.com",
     *   "password": "secret"
     * }
     */
    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<AuthResponse> {
        val response = authService.login(req)
        return ResponseEntity.ok(response)
    }
}
