package com.example.soundstore.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

/**
 * Утилита для работы с JWT-токенами.
 * Генерирует и проверяет токен на основе SecretKey.
 */
@Component
class JwtUtil {
    // Секретная фраза должна быть минимум 32 байта (для HS256)
    private val secretString = "your-256-bit-secret-your-256-bit-secret"
    private val key: SecretKey = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))
    private val validityInMs = 24 * 60 * 60 * 1000L  // 24 часа

    /**
     * Генерирует JWT с subject=email.
     */
    fun generateToken(email: String): String {
        val now = Date()
        val expiry = Date(now.time + validityInMs)
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Извлекает email из токена.
     * Бросит исключение, если токен некорректен или просрочен.
     */
    fun extractEmail(token: String): String =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
}
