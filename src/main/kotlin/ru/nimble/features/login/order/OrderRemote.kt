package ru.nimble.features.login.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    val userId: String
)

