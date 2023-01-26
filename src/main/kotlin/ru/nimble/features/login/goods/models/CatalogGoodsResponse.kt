package ru.nimble.features.login.goods.models

import kotlinx.serialization.Serializable

@Serializable
data class CatalogGoodsResponse(
    val goods: List<CatalogGoods>
)

@Serializable
data class CatalogGoods(
    val name: String,
    val price: Double,
    val logo : String,
    val description : String
)