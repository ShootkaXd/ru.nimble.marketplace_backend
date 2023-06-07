package ru.nimble.features.login.cart

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.nimble.database.cart.Cart


fun Application.configureCartRouting(){

    routing {
        post("/cart/") {
            val cartController = CartController(call)
            cartController.addCart()
        }

        get("/cart/{userId}"){
            val userId = call.parameters["userId"]?: return@get call.respond(HttpStatusCode.BadRequest)
            val cartItems = Cart.getByUserId(userId)
            call.respond(cartItems)
        }

    }

}