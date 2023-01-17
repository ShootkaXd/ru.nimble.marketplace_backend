package ru.nimble.database.user

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object User: Table("users"){
    private val id = User.varchar("id", 100)
    private val email = User.varchar("email", 50)
    private val password = User.varchar("password", 25)
    private val firstName = User.varchar("firstName", 25)
    private val lastName = User.varchar("lastName", 25)


    fun insert(userDTO: UserDTO){
        transaction {
            User.insert {
                it[id] = userDTO.rowId
                it[email] = userDTO.email ?: ""
                it[password] = userDTO.password
                it[firstName] = userDTO.firstName
                it[lastName] = userDTO.lastName
            }
        }
    }

    fun fetchUser(email: String) : UserDTO? {
        return try {
            transaction {
                val userModel = User.select { User.email.eq(email) }.single()
                UserDTO(
                    rowId = userModel[User.id],
                    email = userModel[User.email],
                    password = userModel[password],
                    firstName = userModel[firstName],
                    lastName = userModel[lastName]
                )
            }
        }catch (e:Exception){
            null
        }
    }
}