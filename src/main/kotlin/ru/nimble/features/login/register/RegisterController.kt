package ru.nimble.features.login.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.nimble.database.tokens.TokenDTO
import ru.nimble.database.tokens.Tokens
import ru.nimble.database.user.User
import ru.nimble.database.user.UserDTO
import ru.nimble.utils.isValidEmail
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        if (!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email не является допустимым")
        }

        val userDTO = User.fetchUser(registerReceiveRemote.email)
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "Пользователь уже существует")
        } else {
            val token = UUID.randomUUID().toString()

            try {
                User.insert(
                    UserDTO(
                        email = registerReceiveRemote.email,
                        password = registerReceiveRemote.password,
                        firstName = registerReceiveRemote.firstName,
                        lastName = registerReceiveRemote.lastName
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "Пользователь уже существует")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Не могу создать пользователя${e.localizedMessage}")
            }

            Tokens.insert(
                TokenDTO(
                    rowId = UUID.randomUUID().toString(), email = registerReceiveRemote.email,
                    token = token
                )
            )

            call.respond(RegisterResponseRemote(token = token))
        }
    }
}