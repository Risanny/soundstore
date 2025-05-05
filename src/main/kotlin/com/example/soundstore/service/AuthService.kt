package com.example.soundstore.service

import com.example.soundstore.dto.AuthResponse               // :contentReference[oaicite:0]{index=0}&#8203;:contentReference[oaicite:1]{index=1}
import com.example.soundstore.dto.LoginRequest               // :contentReference[oaicite:2]{index=2}&#8203;:contentReference[oaicite:3]{index=3}
import com.example.soundstore.dto.RegisterRequest            // :contentReference[oaicite:4]{index=4}&#8203;:contentReference[oaicite:5]{index=5}
import com.example.soundstore.model.User                     // :contentReference[oaicite:6]{index=6}&#8203;:contentReference[oaicite:7]{index=7}
import com.example.soundstore.repository.UserRepository      // :contentReference[oaicite:8]{index=8}&#8203;:contentReference[oaicite:9]{index=9}
import com.example.soundstore.security.JwtUtil               // :contentReference[oaicite:10]{index=10}&#8203;:contentReference[oaicite:11]{index=11}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 * Сервис для регистрации и аутентификации пользователей.
 */
@Service
class AuthService(
    private val userRepo: UserRepository,
    private val jwtUtil: JwtUtil
) {
    // BCrypt для надёжного хеширования паролей
    private val passwordEncoder = BCryptPasswordEncoder()

    /**
     * Регистрация нового пользователя:
     * 1) Проверяем, что email ещё не занят.
     * 2) Хешируем пароль.
     * 3) Сохраняем модель User в БД.
     * 4) Генерируем JWT и возвращаем его в ответе.
     */
    fun register(req: RegisterRequest): AuthResponse {
        // 1. Проверка уникальности email
        if (userRepo.findByEmail(req.email) != null) {
            throw IllegalArgumentException("Email is already in use")
        }
        // 2. Хешируем пароль
        val hashed = passwordEncoder.encode(req.password)
        // 3. Создаём и сохраняем пользователя
        val user = userRepo.save(User(email = req.email, password = hashed))
        // 4. Генерируем JWT
        val token = jwtUtil.generateToken(user.email)
        return AuthResponse(token, user.email)
    }

    /**
     * Аутентификация существующего пользователя:
     * 1) Ищем User по email.
     * 2) Сравниваем введённый пароль с хешем в БД.
     * 3) Генерируем JWT и возвращаем.
     */
    fun login(req: LoginRequest): AuthResponse {
        // 1. Получаем пользователя
        val user = userRepo.findByEmail(req.email)
            ?: throw IllegalArgumentException("Invalid credentials")
        // 2. Проверяем пароль
        if (!passwordEncoder.matches(req.password, user.password)) {
            throw IllegalArgumentException("Invalid credentials")
        }
        // 3. Генерируем JWT
        val token = jwtUtil.generateToken(user.email)
        return AuthResponse(token, user.email)
    }
}
