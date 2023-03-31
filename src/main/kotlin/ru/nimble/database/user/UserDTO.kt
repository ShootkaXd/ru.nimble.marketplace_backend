package ru.nimble.database.user

import kotlinx.serialization.Serializable

@Serializable
class UserDTO (
    val rowId: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val salt: String
)

@Serializable
class UserOut(
    val email: String,
    val firstName: String,
    val lastName: String
)
