package ru.nimble.features.login.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.nimble.database.order.Orders

class OrderController(private val call: ApplicationCall){
    suspend fun orderCreate() {
        try {
            // Получение данных из запроса
            val request = call.receive<OrderRequest>()

            // Создание заказа
            val createdOrder = Orders.createOrder(request.userId)

            if (createdOrder != null) {
                // Отправка успешного ответа
                call.respond(HttpStatusCode.Created, createdOrder)
            } else {
                // Отправка ответа об ошибке
                call.respond(HttpStatusCode.BadRequest, "Корзина пуста или произошла ошибка при создании заказа.")
            }
        } catch (e: Exception) {
            // Обработка исключений
            call.respond(HttpStatusCode.InternalServerError, "Произошла ошибка при создании заказа: ${e.message}")
        }
    }
}