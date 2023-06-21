package ru.nimble.database.cart

import kotlinx.serialization.Serializable
import ru.nimble.features.login.goods.models.GoodsResponse

@Serializable
data class CartItem(
    val userId: String,
    val productId: String,
)

@Serializable
data class CartList(
    val productId: String,
    val quantity: Int
)

@Serializable
data class Carts(
    val id: Int,
    val product: GoodsResponse,
    val quantity: Int,
)

