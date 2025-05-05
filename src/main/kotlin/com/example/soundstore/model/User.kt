package com.example.soundstore.model

import jakarta.persistence.*

@Entity
@Table(name = "users")  // имя таблицы в БД
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER
)

enum class Role {
    USER,
    ADMIN
}
