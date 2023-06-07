package ru.nimble.features.login.order

import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureOrderRouting(){

    routing {
        post("/order") {
            val orderController = OrderController(call)
            orderController.createOrder()
        }

        get("/order/{userId}"){
            val orderController = OrderController(call)
            orderController.getUserOrder()
        }

    }

}