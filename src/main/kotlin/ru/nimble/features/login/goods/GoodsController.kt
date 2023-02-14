package ru.nimble.features.login.goods

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.nimble.database.goods.*
import ru.nimble.features.login.goods.models.*
import ru.nimble.utils.TokenCheck

class GoodsController(private val call: ApplicationCall) {
    suspend fun createGoods(){
        val token = call.request.headers["Bearer-Authorization"]
        if (token == null){
            call.respond(HttpStatusCode.Unauthorized, "Not logged in")
            return
        }
        if (TokenCheck.isTokenAdmin(token)){
            val request = call.receive<GoodsRequest>()
            val goods = request.toGoodsModel()
            Goods.insert(goods)
            call.respond(goods.toGoodsResponse())
        }else{
            call.respond(HttpStatusCode.Forbidden, "User is not admin")
        }
    }

    suspend fun listGoods(){
        val search = call.parameters["search"]
        var goods = Goods.fetchAll()
        if (search != null){
            goods = goods.filter{ it.name.contains(search, ignoreCase = true) }
        }
        call.respond(ListResponse<GoodsModel>(
                goods
        ))
    }




}