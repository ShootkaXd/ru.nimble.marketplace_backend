package ru.nimble.utils

import ru.nimble.database.tokens.Tokens

object TokenCheck {

    fun isTokenValid(token: String): Boolean = Tokens.fetchTokens().firstOrNull { it.token == token } != null

    fun isTokenAdmin(token: String): Boolean = token == "bf8487ae-7d47-11ec-90d6-0242ac120003"
}