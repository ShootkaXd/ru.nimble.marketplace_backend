package ru.nimble.features.login.goods

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.nimble.database.goods.*
import ru.nimble.features.login.goods.models.*
import ru.nimble.utils.TokenCheck

class GoodsController(private val call: ApplicationCall) {

    suspend fun fetchAllGoods(){
        val request = call.receive<FetchGoodsRequest>()
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty()) || TokenCheck.isTokenAdmin(token.orEmpty())) {
            call.respond(Goods.fetchGoods().first{ it.name.contains(request.searchQuery, ignoreCase = true)})
        }else{
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun createGoods(){
        val token = call.request.headers["Bearer-Authorization"]
        if (TokenCheck.isTokenAdmin(token.orEmpty())){
            val request = call.receive<CreateGoodsRequest>()
            val goods = request.mapToGoodsDTO()
            Goods.insert(goods)
            call.respond(goods.mapToCreateGoodsResponse())
        }else{
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }

    }

    suspend fun listGoods(){
        val request = call.receive<FetchGoodsRequest>()
        call.respond(FetchGoodsResponse(
            goods = Goods.fetchGoods().filter { it.name.contains(request.searchQuery, ignoreCase = true) }
                .map { it.mapToGoodsResponse() }
        ))
    }

    suspend fun catalogGoodsView(){
        val request = call.receive<FetchGoodsRequest>()
        call.respond(CatalogGoodsResponse(
            goods = Goods.fetchGoods().filter { it.name.contains(request.searchQuery, ignoreCase = true) }
                .map { it.mapCatalogGoods() }
        ))
    }
}