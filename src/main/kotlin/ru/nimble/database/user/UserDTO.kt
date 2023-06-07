package ru.nimble.database.user

import kotlinx.serialization.Serializable

@Serializable
class UserDTO (
    val rowId: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val salt: String,
)
@Serializable
class UserAddress(
    val home_address: String,
    val house_address: String,
    val city: String
)

@Serializable
class UserOut(
    val email: String,
    val firstName: String,
    val lastName: String
)

@Serializable
class UserOr(
    val id: String,
    val email: String,
)