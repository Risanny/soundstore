package com.example.soundstore.controller

import com.example.soundstore.model.Product
import com.example.soundstore.model.Order
import com.example.soundstore.model.OrderStatus
import com.example.soundstore.service.ProductService
import com.example.soundstore.service.OrderService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

/**
 * Веб-админка для управления товарами и заказами через Thymeleaf.
 */
@Controller
@RequestMapping("/admin")
class AdminController(
    private val productService: ProductService,
    private val orderService: OrderService
) {

    /**
     * Список товаров (CRUD для продуктов)
     */
    @GetMapping("/products")
    fun listProducts(model: Model): String {
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "admin/products"
    }

    /**
     * Форма создания нового товара
     */
    @GetMapping("/products/new")
    fun newProductForm(model: Model): String {
        model.addAttribute("product", Product(
            id = 0,
            name = "",
            description = "",
            price = BigDecimal.ZERO,
            imageUrl = "",
            wirelessStandard = "",
            type = "",
            brand = ""
        ))
        return "admin/product_form"
    }

    /**
     * Форма редактирования товара
     */
    @GetMapping("/products/edit/{id}")
    fun editProductForm(@PathVariable id: Long, model: Model): String {
        val product = productService.getProductById(id)
        model.addAttribute("product", product)
        return "admin/product_form"
    }

    /**
     * Сохранение (создание или обновление) товара
     */
    @PostMapping("/products/save")
    fun saveProduct(@ModelAttribute product: Product): String {
        productService.saveOrUpdate(product)
        return "redirect:/admin/products"
    }

    /**
     * Удаление товара по ID
     */
    @GetMapping("/products/delete/{id}")
    fun deleteProduct(@PathVariable id: Long): String {
        productService.deleteById(id)
        return "redirect:/admin/products"
    }

    /**
     * Список заказов
     */
    @GetMapping("/orders")
    fun listOrders(model: Model): String {
        val orders: List<Order> = orderService.getAllOrders()
        model.addAttribute("orders", orders)
        model.addAttribute("statuses", OrderStatus.values())
        return "admin/orders"
    }

    /**
     * Изменение статуса заказа
     */
    @PostMapping("/orders/{id}/status")
    fun updateOrderStatus(
        @PathVariable id: Long,
        @RequestParam status: OrderStatus
    ): String {
        orderService.updateStatus(id, status)
        return "redirect:/admin/orders"
    }
}
