package ru.nimble.features.login.reviews

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureReviewRouting() {
    routing {
        route("/reviews") {
            get("/{goodsId}") {
                ReviewController(call).getReviewsByGoodsId()
            }
            post("/create") {
                ReviewController(call).createReview()
            }
            delete("/{reviewId}") {
                ReviewController(call).deleteReview()
            }
        }
    }
}