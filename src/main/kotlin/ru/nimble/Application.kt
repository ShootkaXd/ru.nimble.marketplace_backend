package ru.nimble

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.websocket.*
import org.jetbrains.exposed.sql.Database
import ru.nimble.features.login.configureLoginRouting
import ru.nimble.features.login.register.configureRegisterRouting
import ru.nimble.plugins.*
import java.time.Duration

fun main() {
    Database.connect("jdbc:postgresql://localhost:5432/Nimble", driver = "org.postgresql.Driver",
        user = "postgres", password = "Uxin2001")


    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {

        install(WebSockets){
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }

        configureRouting()
        configureLoginRouting()
        configureRegisterRouting()
        configureSerialization()
    }.start(wait = true)
}