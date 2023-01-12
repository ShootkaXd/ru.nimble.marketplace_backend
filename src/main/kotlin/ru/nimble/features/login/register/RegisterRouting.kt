package ru.nimble.features.login.register

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.nimble.features.login.LoginController


fun Application.configureRegisterRouting() {

    routing {
        post("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()
        }
    }

    routing {
        webSocket("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()
        }
    }
}