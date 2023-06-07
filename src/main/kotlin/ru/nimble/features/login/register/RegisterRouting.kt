package ru.nimble.features.login.register

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.nimble.database.user.User
import ru.nimble.features.login.LoginController


fun Application.configureRegisterRouting() {

    routing {

        post("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()
        }

        get("/users"){
            call.respond(User.getAll())
        }

        post("/register/address"){
            val registerController = RegisterController(call)
            registerController.registerAddress()
        }

    }


}