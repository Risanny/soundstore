package com.example.soundstore.controller

import com.example.soundstore.dto.OrderRequest
import com.example.soundstore.model.Order
import com.example.soundstore.model.User
import com.example.soundstore.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.security.Principal

/**
 * REST-контроллер для работы с заказами.
 * Оформление заказа и просмотр истории заказов пользователя.
 */
@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {

    /**
     * Оформление нового заказа.
     * Принимает JSON, соответствующий OrderRequest, и возвращает сохранённый Order.
     * Доступ по JWT (POST /api/orders защищён).
     */
    @PostMapping
    fun createOrder(@RequestBody req: OrderRequest): ResponseEntity<Order> =
        try {
            val savedOrder = orderService.createOrder(req)
            ResponseEntity.ok(savedOrder)
        } catch (e: IllegalArgumentException) {
            // Если пользователю не найден (по email) или другой сбой валидации
            ResponseEntity.badRequest().build()
        }

    /**
     * Получение истории заказов пользователя.
     * @param userId ID пользователя, для которого запрашиваем заказы.
     * Доступ по JWT (GET /api/orders защищён).
     */
    @GetMapping
    fun getMyOrders(authentication: Authentication): ResponseEntity<List<Order>> {
        val user: User = authentication.principal as User
        val orders = orderService.getOrdersForUser(user.id)
        return ResponseEntity.ok(orders)
    }

}
