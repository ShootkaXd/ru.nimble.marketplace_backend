package ru.nimble.features.login.goods.models

import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(
    val items: List<T>
)

