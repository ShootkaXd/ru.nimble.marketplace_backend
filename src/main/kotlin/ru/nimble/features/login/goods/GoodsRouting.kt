package ru.nimble.features.login.goods

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureGoodsRouting(){

    routing {
        post("/goods/fetch") {
            val goodsController = GoodsController(call)
            goodsController.fetchAllGoods()
        }

        post("/goods/create") {
            val goodsController = GoodsController(call)
            goodsController.createGoods()
        }

        get("/goods"){
            val goodsList = GoodsController(call)
            goodsList.listGoods()
        }

        get("/goods/catalog"){
            val goodsListCatalog = GoodsController(call)
            goodsListCatalog.catalogGoodsView()
        }
    }


}