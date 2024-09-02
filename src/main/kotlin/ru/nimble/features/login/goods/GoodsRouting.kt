package ru.nimble.features.login.goods

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureGoodsRouting(){

    routing {
        route("/goods") {

            post("/create") {
                val goodsController = GoodsController(call)
                goodsController.createGoods()
            }

            get("/list"){
                val goodsList = GoodsController(call)
                goodsList.listGoods()
            }

            get("/filterByPrice") {
                GoodsController(call).filterGoodsByPrice()
            }
        }
    }
}