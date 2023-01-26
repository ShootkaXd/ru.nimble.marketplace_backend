package ru.nimble.database.user

class UserDTO (
    val rowId: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val salt: String
)