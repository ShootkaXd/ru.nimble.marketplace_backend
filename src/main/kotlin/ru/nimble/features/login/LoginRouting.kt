package ru.nimble.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.nimble.database.user.UserDTO


fun Application.configureLoginRouting() {

    routing {
        post("/login") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }

        get("/user/{id}"){
            val userOutput = LoginController(call)
            userOutput.outUser()
        }
    }

}