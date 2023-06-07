package ru.nimble.database.tokens

import kotlinx.serialization.Serializable

@Serializable
class TokenDTO(
    val rowId: String,
    val email: String,
    val token: String
)
