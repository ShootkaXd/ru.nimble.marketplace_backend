package ru.nimble.security.hashing

import kotlinx.serialization.Serializable

@Serializable
data class SaltedHash(
    val hash: String,
    val salt: String
)
