package ru.nimble

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.jetbrains.exposed.sql.Database
import ru.nimble.features.login.configureLoginRouting
import ru.nimble.features.login.register.configureRegisterRouting
import ru.nimble.plugins.*

fun main() {
    Database.connect("jdbc:postgresql://localhost:5432/Nimble", driver = "org.postgresql.Driver",
        user = "postgres", password = "Uxin2001")


    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureLoginRouting()
        configureRegisterRouting()
        configureSerialization()
    }.start(wait = true)
}