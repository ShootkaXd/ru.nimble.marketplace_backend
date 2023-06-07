package ru.nimble.features.login.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import ru.nimble.database.cart.Cart
import ru.nimble.database.goods.Goods
import ru.nimble.database.order.Orders

@Serializable
data class OrderRequest(val userId: String, val address: String)

class OrderController(private val call: ApplicationCall) {

    suspend fun createOrder(){
        val orderRequest = call.receive<OrderRequest>()

        val cartItems = Cart.getByUserId(orderRequest.userId)
        val totalPrice = cartItems.sumOf { it.product.price }

        val orderId = Orders.create(orderRequest.userId, totalPrice)

        call.respond(orderId)
    }

    suspend fun getUserOrder(){
        val userId = call.parameters["userId"]

        if(userId == null){
            call.respond(HttpStatusCode.BadRequest)
        }else {
            val orders = Orders.getByUserId(userId)
            call.respond(orders)
        }
    }
}