package ru.nimble.features.login.order

import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureOrderRouting(){

    routing {
        route("/orders") {
            post {
                val controller = OrderController(call)
                controller.orderCreate()
            }
        }
    }

}