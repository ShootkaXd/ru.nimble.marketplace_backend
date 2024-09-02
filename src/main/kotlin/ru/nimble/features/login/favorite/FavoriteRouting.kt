package ru.nimble.features.login.favorite

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.nimble.features.favorites.FavoritesController

fun Application.configureFavoriteRouting() {
    routing {
        route("/favorites") {
            post("/add") {
                val controller = FavoritesController(call)
                controller.addFavorite()
            }

            delete("/remove") {
                val controller = FavoritesController(call)
                controller.removeFavorite()
            }

            get("/{userId}") {
                val controller = FavoritesController(call)
                controller.getFavorites()
            }
        }
    }
}
