package ru.nimble.features.login

import kotlinx.serialization.Serializable


@Serializable
data class LoginReceiveRemote(
    val email: String,
    val password: String,
)

@Serializable
data class LoginResponseRemote(
    val userId: String,
    val token: String
)