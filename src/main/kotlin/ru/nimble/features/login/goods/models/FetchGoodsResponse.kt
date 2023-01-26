package ru.nimble.features.login.goods.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchGoodsResponse(
    val goods: List<GoodsResponse>
)

@Serializable
data class GoodsResponse(
    val name : String,
    val price: Double,
    val manufacturer : String,
    val logo : String,
    val grade : Double,
    val description : String,
    val specification : String,
    val availability : Int,
)