package ru.nimble.features.login.goods.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateGoodsRequest (
    val name : String,
    val manufacturer : String,
    val logo : String,
    val grade : Int,
    val description : String,
    val specification : String,
    val availability : Int,
)

@Serializable
data class CreateGoodsResponse(
    val idgoods : String,
    val name : String,
    val manufacturer : String,
    val logo : String,
    val grade : Int,
    val description : String,
    val specification : String,
    val availability : Int,
)