package ru.nimble.features.login.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.nimble.database.order.Orders
import ru.nimble.database.order.ProcessOrderRequest

class OrderController(private val call: ApplicationCall){
    suspend fun orderCreate() {
        try {
            val request = call.receive<OrderRequest>()

            val createdOrder = Orders.createOrder(request.userId)

            if (createdOrder != null) {
                call.respond(HttpStatusCode.Created, createdOrder)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Корзина пуста или произошла ошибка при создании заказа.")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Произошла ошибка при создании заказа: ${e.message}")
        }
    }

    suspend fun updateProcess(){
        val request = call.receive<ProcessOrderRequest>()
        val updated = Orders.processOrder(request.orderId)
        if (updated){
            call.respond(HttpStatusCode.OK, "Заказ принят в обработку")
        }else{
            call.respond(HttpStatusCode.BadRequest, "Не удалось обновить заказ")
        }
    }
}