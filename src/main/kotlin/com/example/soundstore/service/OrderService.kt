package com.example.soundstore.service

import com.example.soundstore.dto.OrderRequest
import com.example.soundstore.model.Order
import com.example.soundstore.model.OrderStatus
import com.example.soundstore.repository.OrderRepository
import com.example.soundstore.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

/**
 * Сервис для оформления и управления заказами.
 */
@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper,
    private val mailSender: JavaMailSender,
    @Value("\${app.soundstore.email}")
    private val adminEmail: String
) {

    /** Создаёт новый заказ и отправляет уведомление админу. */
    fun createOrder(req: OrderRequest): Order {
        val user = userRepository.findByEmail(req.email)
            ?: throw IllegalArgumentException("User not found: \${req.email}")
        val itemsJson = objectMapper.writeValueAsString(req.items)
        val order = orderRepository.save(Order(user = user, itemsJson = itemsJson))

        val msg = SimpleMailMessage().apply {
            setTo(adminEmail)
            setSubject("Новый заказ #\${order.id}")
            setText(buildEmailText(order, req))
        }
        mailSender.send(msg)

        return order
    }

    /** Возвращает все заказы пользователя по его ID. */
    fun getOrdersForUser(userId: Long): List<Order> = orderRepository.findByUserId(userId)

    /** Возвращает все заказы (для админки). */
    fun getAllOrders(): List<Order> = orderRepository.findAll()

    /** Обновляет статус заказа и возвращает обновлённый объект. */
    fun updateStatus(orderId: Long, newStatus: OrderStatus): Order {
        val order = orderRepository.findById(orderId)
            .orElseThrow { NoSuchElementException("Order not found: id=$orderId") }
        order.status = newStatus
        return orderRepository.save(order)
    }

    // Вспомогательная функция генерации текста письма
    private fun buildEmailText(order: Order, req: OrderRequest): String =
        StringBuilder().apply {
            append("Поступил новый заказ №\${order.id}\n")
            append("Покупатель: \${req.name} (\${req.email})\n")
            append("Адрес: \${req.address}\n")
            append("Телефон: \${req.phone}\n\n")
            append("Состав заказа:\n")
            req.items.forEach { item ->
                append("— Товар ID \${item.productId}, количество: \${item.quantity}\n")
            }
            append("\nСтатус: \${order.status}\n")
        }.toString()
}
