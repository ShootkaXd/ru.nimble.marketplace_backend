package ru.nimble

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.jetbrains.exposed.sql.Database
import ru.nimble.database.namePostman
import ru.nimble.features.login.configureLoginRouting
import ru.nimble.features.login.goods.configureGoodsRouting
import ru.nimble.features.login.register.configureRegisterRouting
import ru.nimble.plugins.*
import ru.nimble.database.passwordDB
import ru.nimble.features.login.cart.configureCartRouting
import ru.nimble.features.login.favorite.configureFavoriteRouting
import ru.nimble.features.login.order.configureOrderRouting
import ru.nimble.features.login.reviews.configureReviewRouting


fun main() {
    Database.connect("jdbc:postgresql://localhost:5432/Nimble", driver = "org.postgresql.Driver",
        user = namePostman, password = passwordDB)


    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {


        configureGoodsRouting()
        configureRouting()
        configureLoginRouting()
        configureRegisterRouting()
        configureCartRouting()
        configureOrderRouting()
        configureSerialization()
        configureFavoriteRouting()
        configureReviewRouting()
    }.start(wait = true)
}