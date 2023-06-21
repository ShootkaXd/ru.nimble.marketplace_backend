package ru.nimble.database.cart

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nimble.database.goods.Goods
import ru.nimble.database.goods.toGoodsResponse
import ru.nimble.database.goods.toGoodsResponseCart
import ru.nimble.database.user.User


object Cart: Table() {
    val id = integer("id").autoIncrement()
    private val userId = varchar("user_id",100).references(User.id)
    private val productId = varchar("product_id", 100).references(Goods.vendorCode)
    private val quantity = integer("quantity")

    override val primaryKey = PrimaryKey(id)

    fun addToCart(cartItem: CartItem){
        transaction {
            val item = Cart.select { Cart.productId eq cartItem.productId and (Cart.userId eq cartItem.userId)}.singleOrNull()?.let {
                object {
                    val quantity: Int = it[Cart.quantity]
                }
            }
            if (item?.quantity != null) {
                Cart.update({Cart.productId eq cartItem.productId and (Cart.userId eq cartItem.userId)}) {
                    with(SqlExpressionBuilder) {
                        it[Cart.quantity] = Cart.quantity + 1
                    }
                }
            } else {
                Cart.insert {
                    it[userId] = cartItem.userId
                    it[productId] = cartItem.productId
                    it[quantity] = 1
                }
            }
        }
    }

    fun clearCart(){
        transaction {
            Cart.deleteAll()
        }
    }

    fun getByUserId(userId: String): List<Carts>{
        return transaction {
            Cart.select{ Cart.userId eq userId}.map {
                Carts(
                    product = Goods.getGoodsByVendorCode(it[productId]).toGoodsResponseCart(),
                    quantity = it[quantity],
                    id = it[Cart.id]
                )
            }
        }
    }

    fun delete(id: Int){
        transaction {
            Cart.deleteWhere { Cart.id eq id }
        }
    }

    fun deleteByUserId(userId: String){
        transaction {
            Cart.deleteWhere { Cart.userId eq userId }
        }
    }


}


