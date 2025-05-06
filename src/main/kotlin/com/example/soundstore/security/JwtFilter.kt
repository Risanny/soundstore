package com.example.soundstore.security

import com.example.soundstore.repository.UserRepository
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Фильтр, который проверяет заголовок Authorization на наличие Bearer-токена,
 * валидирует его и, если всё ок, помещает Authentication в контекст Spring Security.
 */
@Component
class JwtFilter(
    private val jwtUtil: JwtUtil,
    private val userRepo: UserRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. Получаем заголовок Authorization
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substringAfter("Bearer ").trim()
            try {
                // 2. Извлекаем email из токена
                val email = jwtUtil.extractEmail(token)
                // 3. Если аутентификация ещё не установлена
                if (SecurityContextHolder.getContext().authentication == null) {
                    // 4. Загружаем пользователя из БД
                    val user = userRepo.findByEmail(email)
                    if (user != null) {
                        // 5. Создаём объект Authentication
                        val auth = UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
                        )
                        auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                        // 6. Ставим его в контекст
                        SecurityContextHolder.getContext().authentication = auth
                    }
                }
            } catch (e: JwtException) {
                // токен некорректен или просрочен — просто не аутентифицируем
            }
        }
        // 7. Продолжаем цепочку фильтров
        filterChain.doFilter(request, response)
    }
}
