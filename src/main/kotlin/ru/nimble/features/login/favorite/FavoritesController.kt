package ru.nimble.features.favorites

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nimble.database.favoriteGoods.Favorites.addGoodsToFavorites
import ru.nimble.database.favoriteGoods.Favorites.getUserFavorites
import ru.nimble.database.favoriteGoods.Favorites.removeGoodsFromFavorites

class FavoritesController(private val call: ApplicationCall) {

    suspend fun addFavorite() {
        val request = call.receive<FavoriteRequest>()
        val userId = request.userId
        val goodsId = request.goodsId

        try {
            transaction {
                addGoodsToFavorites(userId, goodsId)
            }
            call.respond(HttpStatusCode.OK, "Товар добавлен в избранное")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Ошибка при добавлении товара в избранное: ${e.localizedMessage}")
        }
    }

    suspend fun removeFavorite() {
        val request = call.receive<FavoriteRequest>()
        val userId = request.userId
        val goodsId = request.goodsId

        try {
            transaction {
                removeGoodsFromFavorites(userId, goodsId)

            }
            call.respond(HttpStatusCode.OK, "Товар удален из избранного")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Ошибка при удалении товара из избранного: ${e.localizedMessage}")
        }
    }

    suspend fun getFavorites() {
        val userId = call.parameters["userId"] ?: return call.respond(HttpStatusCode.BadRequest, "Отсутствует userId")

        try {
            val favorites = transaction { getUserFavorites(userId) }
            call.respond(HttpStatusCode.OK, favorites)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Ошибка при получении избранных товаров: ${e.localizedMessage}")
        }
    }
}
