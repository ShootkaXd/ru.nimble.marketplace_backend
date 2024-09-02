package ru.nimble.features.favorites

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequest(
    val userId: String,
    val goodsId: String
)