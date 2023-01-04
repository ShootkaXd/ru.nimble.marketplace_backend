package ru.nimble.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.nimble.database.tokens.TokenDTO
import ru.nimble.database.tokens.Tokens
import ru.nimble.database.user.User
import java.util.*

class LoginController(private val call: ApplicationCall){

    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = User.fetchUser(receive.email)
        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "Пользователь не существует")
        } else {
            if (userDTO.password == receive.password) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(),
                        email = receive.email,
                        token = token
                    )
                )
                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Неправильный пароль")
            }
        }
    }
}