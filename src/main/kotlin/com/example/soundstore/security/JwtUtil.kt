package com.example.soundstore.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

/**
 * Утилита для работы с JWT-токенами.
 * Генерирует токен по email и умеет извлекать email из существующего токена.
 *
 * В продакшене секретную фразу нужно хранить вне кода (в переменных окружения или в vault).
 */
@Component
class JwtUtil {

    // Секретная фраза для подписи токена. Для разработки достаточно этой константы.
    private val secretKey = "very-secret-dev-key"

    // Время жизни токена в миллисекундах (24 часа)
    private val validityInMs = 24 * 60 * 60 * 1000L

    /**
     * Генерирует JWT-токен, где Subject = email.
     *
     * @param email Email пользователя, который будет зашит в токен.
     * @return Подписанный JWT-токен.
     */
    fun generateToken(email: String): String {
        val now = Date()
        val expiry = Date(now.time + validityInMs)
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    /**
     * Извлекает email (Subject) из переданного токена.
     *
     * @param token JWT-токен в формате "xxx.yyy.zzz"
     * @return Email, сохранённый в токене.
     * @throws io.jsonwebtoken.JwtException если токен некорректен или просрочен.
     */
    fun extractEmail(token: String): String =
        Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
            .subject
}
