package ru.nimble.database.order

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nimble.database.cart.Cart
import java.time.LocalDateTime

object Orders: Table("orders") {
    val id = integer("id").autoIncrement()
    private val userId = varchar("user_id",100)
    private val totalPrice = double("total_price")
    private val status = varchar("status", length = 20)
    private val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id)


    fun create(userId: String, total: Double){
        transaction {
            Orders.insert {
                it[Orders.userId] = userId
                it[totalPrice] = total
                it[status] = "Создан"
                it[createdAt] = LocalDateTime.now()
            }
            Cart.deleteByUserId(userId)

        }

    }

    fun getByUserId(userId: String): List<OrderModel>{
        return transaction {
            select {Orders.userId eq userId}.map{
                OrderModel(
                    id = it[Orders.id],
                    userId = it[Orders.userId],
                    totalPrice = it[totalPrice],
                    status = it[status],
                    createdAt = LocalDateTime.now()
                )
            }
        }
    }
}