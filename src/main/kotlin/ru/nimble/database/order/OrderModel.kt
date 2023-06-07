package ru.nimble.database.order


import java.time.LocalDateTime

class OrderModel(
    val id: Int,
    val userId: String,
    val totalPrice: Double,
    val status: String,
    val createdAt: LocalDateTime
)

