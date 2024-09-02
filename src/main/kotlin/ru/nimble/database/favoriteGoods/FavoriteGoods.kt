package ru.nimble.database.favoriteGoods
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nimble.database.goods.Goods
import ru.nimble.database.goods.GoodsModel
import ru.nimble.database.user.User
object Favorites : Table("favorites") {
    val userId = varchar("userId", 100).references(User.id)
    val goodsId = varchar("goodsId", 100).references(Goods.vendorCode)

    init {
        index(true, userId, goodsId)
    }
    fun addGoodsToFavorites(userId: String, goodsId: String) {
        transaction {
            Favorites.insert {
                it[Favorites.userId] = userId
                it[Favorites.goodsId] = goodsId
            }
        }
    }

    fun removeGoodsFromFavorites(userId: String, goodsId: String) {
        transaction {
            Favorites.deleteWhere {
                (Favorites.userId eq userId) and (Favorites.goodsId eq goodsId)
            }
        }
    }

    fun getUserFavorites(userId: String): List<GoodsModel> {
        return transaction {
            (Favorites innerJoin Goods)
                .select { Favorites.userId eq userId }
                .map {
                    val id = it[Goods.id].toString()
                    val name = it[Goods.name]
                    val price = it[Goods.price]
                    val manufacturer = it[Goods.manufacturer]
                    val logo = it[Goods.logo]
                    val grade = it[Goods.grade]
                    val description = it[Goods.description]
                    val specification = it[Goods.specification]
                    val availability = it[Goods.availability]
                    val vendorCode = it[Goods.vendorCode]

                    // Создайте и верните GoodsModel
                    GoodsModel(
                        id = id,
                        name = name,
                        price = price,
                        manufacturer = manufacturer,
                        logo = logo,
                        grade = grade,
                        description = description,
                        specification = specification,
                        availability = availability,
                        vendorCode = vendorCode
                    )
                }
        }
    }

}
