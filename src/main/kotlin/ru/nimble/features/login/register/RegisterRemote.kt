package ru.nimble.features.login.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReceiveRemote(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)

@Serializable
data class RegisterReceiveRemoteAddress(
    val home_address: String,
    val house_address: String,
    val city: String
)

@Serializable
data class RegisterResponseRemote(
    val token: String
)