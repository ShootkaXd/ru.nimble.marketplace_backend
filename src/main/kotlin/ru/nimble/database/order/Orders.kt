package ru.nimble.database.order

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nimble.database.cart.Cart
import ru.nimble.database.goods.Goods
import ru.nimble.database.order.Order
import ru.nimble.database.user.User
import java.time.LocalDateTime

object Orders : IntIdTable() {
    val userId = varchar("user_id", 100).references(User.id)
    val totalAmount = decimal("total_amount", 10, 2)
    val createdAt = datetime("created_at")

    fun createOrder(userId: String): Order? {
        return transaction {
            // Получение всех товаров из корзины пользователя
            val cartItems = Cart.getByUserId(userId)

            if (cartItems.isEmpty()) return@transaction null // Корзина пуста

            // Расчет общей суммы заказа
            val totalAmount = cartItems.sumOf { it.product.price * it.quantity }

            // Вставка заказа в таблицу Orders и получение сгенерированного ID
            val orderId = Orders.insertAndGetId {
                it[this.userId] = userId
                it[this.totalAmount] = totalAmount.toBigDecimal()
                it[this.createdAt] = LocalDateTime.now()
            }.value

            // Вставка каждого товара в таблицу OrderItems
            cartItems.forEach { cartItem ->
                OrderItems.insert {
                    it[this.orderId] = orderId // Этот параметр должен быть типа Int
                    it[productId] = cartItem.product.vendorCode
                    it[quantity] = cartItem.quantity
                    it[price] = cartItem.product.price.toBigDecimal()
                }
            }

            // Очистка корзины пользователя
            Cart.deleteByUserId(userId)

            // Возвращаем данные созданного заказа
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
                createdAt = LocalDateTime.now().toString() // Используем LocalDateTime.now() здесь тоже
            )
        }
    }
}

object OrderItems : IntIdTable() {
    val orderId = integer("order_id").references(Orders.id)
    val productId = varchar("product_id", 100).references(Goods.vendorCode)
    val quantity = integer("quantity")
    val price = decimal("price", 10, 2)
}