package ru.nimble.features.login.order

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.nimble.database.order.Orders


fun Application.configureOrderRouting(){

    routing {
        route("/orders") {
            get {
                val orders = Orders.getAllOrders()
                call.respond(orders)
            }

            post("/create") {
                val controller = OrderController(call)
                controller.orderCreate()
            }
            post("/process"){
                val controller = OrderController(call)
                controller.updateProcess()
            }
        }
    }
}