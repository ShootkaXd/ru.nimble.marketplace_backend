package ru.nimble.database.tokens

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nimble.database.user.UserDTO

object Tokens: Table(){
    private val id = Tokens.varchar("id", 50)
    private val email = Tokens.varchar("email", 50)
    private val token = Tokens.varchar("token", 50)


    fun insert(TokenDTO: TokenDTO){
        transaction {
            Tokens.insert {
                it[id] = TokenDTO.rowId
                it[email] = TokenDTO.email
                it[token] = TokenDTO.token
            }
        }
    }

    fun fetchTokens(): List<TokenDTO> {
        return try {
            transaction {
                Tokens.selectAll().toList()
                    .map {
                        TokenDTO(
                            rowId = it[Tokens.id],
                            token = it[Tokens.token],
                            email = it[Tokens.email]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}