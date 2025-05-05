package com.example.soundstore.repository

import com.example.soundstore.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByCreatedAtAfter(date: LocalDateTime): List<Product>
}
