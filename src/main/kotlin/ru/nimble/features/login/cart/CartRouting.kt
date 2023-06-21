package ru.nimble.features.login.cart

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.nimble.database.cart.Cart
import ru.nimble.database.cart.Carts
import ru.nimble.database.goods.Goods
import ru.nimble.database.goods.GoodsModel
import ru.nimble.features.login.goods.models.ListResponse


fun Application.configureCartRouting(){

    routing {
        post("/cart/") {
            val cartController = CartController(call)
            cartController.addCart()
        }

        get("/cart/{userId}"){
            val userId = call.parameters["userId"]?: return@get call.respond(HttpStatusCode.BadRequest)
            val cartItems = Cart.getByUserId(userId)

            call.respond(ListResponse<Carts>(cartItems))

        }

        delete("/cart/{id}"){
            val id = call.parameters["id"]?: return@delete call.respond(HttpStatusCode.BadRequest)
            Cart.delete(id.toInt())
            call.respond(HttpStatusCode.OK)
        }

    }

}