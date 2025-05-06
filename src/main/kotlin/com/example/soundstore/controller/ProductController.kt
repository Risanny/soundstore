package com.example.soundstore.controller

import com.example.soundstore.model.Product
import com.example.soundstore.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST-контроллер для работы с товарами.
 * Публичные методы для получения каталога и новинок.
 */
@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    /**
     * Возвращает все товары.
     * Доступно без авторизации.
     */
    @GetMapping
    fun getAll(): ResponseEntity<List<Product>> =
        ResponseEntity.ok(productService.getAllProducts())

    /**
     * Возвращает товар по его ID.
     * Если товара нет — возвращает 404 Not Found.
     */
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Product> =
        try {
            ResponseEntity.ok(productService.getProductById(id))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }

    /**
     * Возвращает товары, добавленные за последние 7 дней.
     * Доступно без авторизации.
     */
    @GetMapping("/new")
    fun getNew(): ResponseEntity<List<Product>> =
        ResponseEntity.ok(productService.getNewProducts())
}
