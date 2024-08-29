package ru.nimble.database.order


import kotlinx.serialization.Serializable


@Serializable
data class OrderItem(
    val productId: String,
    val quantity: Int,
    val price: Double
)

@Serializable
data class Order(
    val id: Int,
    val userId: String,
    val totalAmount: Double,
    val items: List<OrderItem>,
    val createdAt: String
)
