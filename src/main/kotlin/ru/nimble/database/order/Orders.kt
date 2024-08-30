package ru.nimble.database.order

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nimble.database.cart.Cart
import ru.nimble.database.goods.Goods
import ru.nimble.database.user.User
import java.time.LocalDateTime

object Orders : IntIdTable() {
    val userId = varchar("user_id", 100).references(User.id)
    val totalAmount = decimal("total_amount", 10, 2)
    val createdAt = datetime("created_at")
    val status = varchar("status", 50).default("Создан")

    fun createOrder(userId: String): Order? {
        return transaction {
            val cartItems = Cart.getByUserId(userId)

            if (cartItems.isEmpty()) return@transaction null // Корзина пуста

            val totalAmount = cartItems.sumOf { it.product.price * it.quantity }

            val orderId = Orders.insertAndGetId {
                it[this.userId] = userId
                it[this.totalAmount] = totalAmount.toBigDecimal()
                it[this.createdAt] = LocalDateTime.now()
            }.value

            cartItems.forEach { cartItem ->
                OrderItems.insert {
                    it[this.orderId] = orderId
                    it[productId] = cartItem.product.vendorCode
                    it[quantity] = cartItem.quantity
                    it[price] = cartItem.product.price.toBigDecimal()
                }
            }

            Cart.deleteByUserId(userId)

            Order(
                id = orderId,
                userId = userId,
                totalAmount = totalAmount,
                items = cartItems.map {
                    OrderItem(
                        productId = it.product.vendorCode,
                        quantity = it.quantity,
                        price = it.product.price
                    )
                },
                createdAt = LocalDateTime.now().toString()
            )
        }
    }
    fun processOrder(orderId: Int): Boolean {
        return transaction {
            Orders.update({ Orders.id eq orderId }) {
                it[status] = "Обработка"
            } > 0
        }
    }
}

object OrderItems : IntIdTable() {
    val orderId = integer("order_id").references(Orders.id)
    val productId = varchar("product_id", 100).references(Goods.vendorCode)
    val quantity = integer("quantity")
    val price = decimal("price", 10, 2)
}