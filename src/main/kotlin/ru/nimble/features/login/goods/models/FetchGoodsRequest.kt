package ru.nimble.features.login.goods.models

import kotlinx.serialization.Serializable

@Serializable
class FetchGoodsRequest(
    val searchQuery: String
)
