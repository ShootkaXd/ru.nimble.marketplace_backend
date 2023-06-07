package ru.nimble.database.goods

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun ResultRow.toGoods(): GoodsModel = GoodsModel(
    id = this[Goods.id].toString(),
    name = this[Goods.name],
    price = this[Goods.price],
    manufacturer = this[Goods.manufacturer],
    logo = this[Goods.logo],
    grade = this[Goods.grade],
    description = this[Goods.description],
    specification = this[Goods.specification],
    availability = this[Goods.availability],
    vendorCode = this[Goods.vendorCode]
)
object Goods : UUIDTable(name = "goods") {
    val name = Goods.varchar("name", 100)
    val price = Goods.double("price")
    val manufacturer = Goods.varchar("manufacturer", 50)
    val logo = Goods.varchar("logo", 500)
    val grade = Goods.double("grade")
    val description = Goods.varchar("description", 500)
    val specification = Goods.varchar("specification", 500)
    val availability = Goods.integer("availability")
    val vendorCode = Goods.varchar("vendorCode", 100)

    init {
            index(true, name, specification)
    }


    fun insert(GoodsDTO: GoodsModel){
        transaction {
            Goods.insert(){
                it[name] = GoodsDTO.name
                it[price] = GoodsDTO.price
                it[manufacturer] = GoodsDTO.manufacturer
                it[logo] = GoodsDTO.logo
                it[grade] = GoodsDTO.grade
                it[description] = GoodsDTO.description
                it[specification] = GoodsDTO.specification
                it[availability] = GoodsDTO.availability
                it[vendorCode] = GoodsDTO.vendorCode
            }
        }
    }

    fun fetchAll(): List<GoodsModel> {
        return try {
            transaction {
                Goods.selectAll()
                    .map {
                        it.toGoods()
                    }

            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    fun getGoodsByVendorCode(vendorCode: String) : GoodsModel{
        return Goods
            .select{ Goods.vendorCode eq vendorCode }
            .single()
            .toGoods()
    }

}