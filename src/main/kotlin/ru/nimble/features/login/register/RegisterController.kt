package ru.nimble.features.login.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.nimble.database.tokens.TokenDTO
import ru.nimble.database.tokens.Tokens
import ru.nimble.database.user.User
import ru.nimble.database.user.UserAddress
import ru.nimble.database.user.UserDTO
import ru.nimble.security.hashing.SaltedHash
import ru.nimble.utils.isValidEmail
import java.security.SecureRandom
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
                        rowId = UUID.randomUUID().toString(),
                        email = registerReceiveRemote.email,
                        password = generateSaltedHash(registerReceiveRemote.password, 32).hash,
                        firstName = registerReceiveRemote.firstName,
                        lastName = registerReceiveRemote.lastName,
                        salt = generateSaltedHash(registerReceiveRemote.password, 32).salt,
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "Пользователь уже существует")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Не могу создать пользователя ${e.localizedMessage}")
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
    private fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex(value)
        return SaltedHash(
            hash = hash,
            salt = saltAsHex
        )
    }

    suspend fun registerAddress(){
        val id = call.request.headers["id"]
        val userAddress = call.receive<UserAddress>()
        if (id != null){
            User.insertAddress(userAddress, id)
            call.respond(HttpStatusCode.OK)
        }else{
            call.respond(HttpStatusCode.BadRequest)
        }
    }
//    suspend fun registerAddress(){
//    val registerReceiveRemoteAddress = call.receive<RegisterReceiveRemoteAddress>()
//    try {
//        User.insertAddress(
//            UserAddress(
//                home_address = registerReceiveRemoteAddress.home_address,
//                house_address = registerReceiveRemoteAddress.house_address,
//                city = registerReceiveRemoteAddress.city
//            )
//        )
//    } catch (e: ExposedSQLException) {
//        call.respond(HttpStatusCode.Conflict, "Пользователь уже существует")
//    } catch (e: Exception) {
//        call.respond(HttpStatusCode.BadRequest, "Не могу создать пользователя ${e.localizedMessage}")
//    }
//    }
}