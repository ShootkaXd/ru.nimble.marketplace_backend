package ru.nimble.database.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nimble.database.tokens.TokenDTO
import ru.nimble.database.tokens.Tokens
import java.util.UUID

object User: Table("users") {
    val id = User.varchar("id", 100)
    private val email = User.varchar("email", 50)
    private val password = User.varchar("password", 100)
    private val firstName = User.varchar("firstName", 25)
    private val lastName = User.varchar("lastName", 25)
    private val salt = User.varchar("salt", 100)
    private val home_address = User.varchar("home_address", 150)
    private val house_address = User.varchar("house_address", 150)
    private val city = User.varchar("city", 150)


    fun insert(userDTO: UserDTO) {
        transaction {
            User.insert {
                it[id] = userDTO.rowId
                it[email] = userDTO.email ?: ""
                it[password] = userDTO.password
                it[firstName] = userDTO.firstName
                it[lastName] = userDTO.lastName
                it[salt] = userDTO.salt
            }
        }
    }

    fun fetchUser(email: String): UserDTO? {
        return try {
            transaction {
                val userModel = User.select { User.email.eq(email) }.single()
                UserDTO(
                    rowId = userModel[User.id],
                    email = userModel[User.email],
                    password = userModel[password],
                    firstName = userModel[firstName],
                    lastName = userModel[lastName],
                    salt = userModel[salt],
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getAll(): ArrayList<UserOut> {
        val users: ArrayList<UserOut> = arrayListOf()
        transaction {
            User.selectAll().map {
                users.add(
                    UserOut(
                        email = it[User.email],
                        firstName = it[User.firstName],
                        lastName = it[User.lastName]
                    )
                )
            }
        }
        return users
    }


    fun update(userOut: UserOut, id: String){
        transaction {
            User.update({User.id eq id}){
                it[email] = userOut.email
                it[firstName] = userOut.firstName
                it[lastName] = userOut.lastName
            }
        }
    }


    fun userOut(id: String):List<UserOut>{
        return try{
            transaction {
                User.select{ (User.id eq id) }.toList()
                    .map{
                        UserOut(
                            email = it[email],
                            firstName = it[firstName],
                            lastName = it[lastName]
                        )
                    }
            }
        }catch (e:Exception){
            emptyList()
        }
    }

    fun insertAddress(userAddress: UserAddress, id: String) {
        transaction {
            User.update({User.id eq id}) {
                it[home_address] = userAddress.home_address
                it[house_address] = userAddress.house_address
                it[city] = userAddress.city
            }
        }
    }

}
