package com.example.soundstore.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,
    @Column(nullable = false)
    val itemsJson: String,
    @Enumerated(EnumType.STRING)
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class OrderStatus { PENDING, SHIPPED, DELIVERED, CANCELLED }
