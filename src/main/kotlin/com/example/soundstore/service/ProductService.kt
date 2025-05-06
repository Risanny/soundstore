package com.example.soundstore.service

import com.example.soundstore.model.Product
import com.example.soundstore.repository.ProductRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Сервис для работы с товарами.
 */
@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    /** Возвращает все доступные товары. */
    fun getAllProducts(): List<Product> = productRepository.findAll()

    /** Возвращает товар по его ID или выбрасывает NoSuchElementException. */
    fun getProductById(id: Long): Product =
        productRepository.findById(id).orElseThrow { NoSuchElementException("Product not found: id=$id") }

    /** Возвращает товары, добавленные за последние 7 дней. */
    fun getNewProducts(): List<Product> {
        val oneWeekAgo = LocalDateTime.now().minusDays(7)
        return productRepository.findByCreatedAtAfter(oneWeekAgo)
    }

    /** Сохраняет новый или обновляет существующий товар. */
    fun saveOrUpdate(product: Product): Product = productRepository.save(product)

    /** Удаляет товар по ID. */
    fun deleteById(id: Long) {
        if (!productRepository.existsById(id)) {
            throw NoSuchElementException("Product not found: id=$id")
        }
        productRepository.deleteById(id)
    }
}