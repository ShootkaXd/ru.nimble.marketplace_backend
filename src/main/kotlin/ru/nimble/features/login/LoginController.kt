package ru.nimble.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import ru.nimble.database.tokens.TokenDTO
import ru.nimble.database.tokens.Tokens
import ru.nimble.database.user.User
import ru.nimble.features.login.register.RegisterReceiveRemote
import ru.nimble.security.hashing.SaltedHash
import java.security.SecureRandom
import java.util.*

class LoginController(private val call: ApplicationCall){

    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = User.fetchUser(receive.email)
        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "Пользователь не существует")
        } else {
            if (userDTO.password == generateSaltedHash(receive.password, 32).hash) {
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

    suspend fun outUser(){
        val id = call.parameters["id"]
        if(id != null){
            call.respond(User.userOut(id))
            call.respond(HttpStatusCode.OK, "Пользователь найден")
        }else{
            call.respond(HttpStatusCode.BadRequest, "Пользователь отсудствует")
        }
    }

    private fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex(value)
        return SaltedHash(
            hash = hash,
            salt = saltAsHex
        )
    }

}