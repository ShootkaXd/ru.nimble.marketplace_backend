package ru.nimble.features.login.cart

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.nimble.database.cart.Cart.addToCart
import ru.nimble.database.cart.CartItem


class CartController(private val call: ApplicationCall) {

    suspend fun addCart() {
        val cartItem = call.receive<CartItem>()

        try{
            addToCart(cartItem)
            call.respond(HttpStatusCode.OK, "Товар добавлен в корзину")
        }catch (e: IllegalArgumentException){
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка")
        }
    }


}