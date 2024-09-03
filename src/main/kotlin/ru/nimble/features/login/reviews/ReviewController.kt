package ru.nimble.features.login.reviews

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.nimble.database.reviews.ReviewModel
import ru.nimble.database.reviews.Reviews

class ReviewController(private val call: ApplicationCall) {

    suspend fun getReviewsByGoodsId() {
        val goodsId = call.parameters["goodsId"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing goodsId")
        val reviews = Reviews.getReviewsByGoodsId(goodsId)
        call.respond(reviews)
    }

    suspend fun createReview() {
        val reviewRequest = call.receive<ReviewModel>()
        Reviews.insertReview(reviewRequest)
        call.respond(HttpStatusCode.Created)
    }

    suspend fun deleteReview() {
        val reviewId = call.parameters["reviewId"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing reviewId")
        Reviews.deleteReview(reviewId)
        call.respond(HttpStatusCode.NoContent)
    }
}