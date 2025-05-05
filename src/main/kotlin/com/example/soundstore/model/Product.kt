package com.example.soundstore.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,
    val description: String? = null,

    @Column(nullable = false)
    val price: BigDecimal,
    val imageUrl: String? = null,
    val wirelessStandard: String? = null,
    val type: String? = null,
    val brand: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
