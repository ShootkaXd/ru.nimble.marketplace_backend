package ru.nimble.database.reviews

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nimble.database.goods.Goods
import ru.nimble.database.goods.GoodsModel
import ru.nimble.database.goods.toGoods
import ru.nimble.database.user.User
import java.util.*

object Reviews : UUIDTable(name = "reviews") {
    val goodsId = varchar("goods_id", 100).references(Goods.vendorCode)
    val userId = varchar("user_id", 100).references(User.id)
    val text = varchar("text", 1000)
    val photos = varchar("photos", 2000).nullable()
    val rating = double("rating")

    init {
        index(true, goodsId, userId)
    }

    fun insertReview(reviewDTO: ReviewModel) {
        transaction {
            Reviews.insert {
                it[goodsId] = reviewDTO.goodsId
                it[userId] = reviewDTO.userId
                it[text] = reviewDTO.text
                it[photos] = reviewDTO.photos.joinToString(",")
                it[rating] = reviewDTO.rating
            }
        }
    }

    fun getReviewsByGoodsId(goodsId: String): List<ReviewModel> {
        return transaction {
            Reviews.select { Reviews.goodsId eq goodsId}
                .map { it.toReview() }
        }
    }

    fun ResultRow.toReview(): ReviewModel = ReviewModel(
        id = this[Reviews.id].toString(),
        goodsId = this[Reviews.goodsId].toString(),
        userId = this[Reviews.userId].toString(),
        text = this[Reviews.text],
        photos = this[Reviews.photos]?.split(",") ?: emptyList(),
        rating = this[Reviews.rating]
    )

    fun deleteReview(reviewId: String) {
        transaction {
            Reviews.deleteWhere { Reviews.id eq UUID.fromString(reviewId) }
        }
    }

    fun getGoodsWithReviewsById(goodsId: String): GoodsModel? {
        return transaction {
            val goodsRow = Goods.select { Goods.id eq UUID.fromString(goodsId) }.singleOrNull()
            goodsRow?.let {
                val goods = it.toGoods()
                val reviews = Reviews.getReviewsByGoodsId(goodsId)
                goods.copy(reviews = reviews)
            }
        }
    }

    fun addReviewToGoods(reviewDTO: ReviewModel) {
        insertReview(reviewDTO)
    }
}
