package com.example.soundstore.dto

/**
 * DTO для одного элемента заказа:
 * @property productId ID товара, добавляемого в заказ.
 * @property quantity  Количество штук данного товара.
 */
data class OrderItemRequest(
    val productId: Long,
    val quantity: Int
)

data class OrderRequest(
    val items: List<OrderItemRequest>,
    val name: String,
    val address: String,
    val phone: String,
    val email: String
)
