package ru.nimble.database.reviews

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

@Serializable
data class ReviewModel(
    val id: String,
    val goodsId: String,
    val userId: String,
    val text: String,
    val photos: List<String>,
    val rating: Double
)