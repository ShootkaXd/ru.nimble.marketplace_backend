package ru.nimble.database.goods

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


//enum class GoodsType {
//    ELECTRONICS, FURNITURE, CLOTHING, FOOD,
//}
enum class SortDirection {
    ASC, DESC
}
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
    vendorCode = this[Goods.vendorCode],
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

//    fun getGoodsByType(type: GoodsType): List<GoodsModel> {
//        return transaction {
//            Goods.select { Goods.type eq type.name }
//                .map { it.toGoods() }
//        }
//    }

    // Фильтрация по цене с сортировкой
    fun getGoodsByPriceRange(minPrice: Double, maxPrice: Double, sortDirection: SortDirection = SortDirection.ASC): List<GoodsModel> {
        return transaction {
            Goods.select { (Goods.price greaterEq minPrice) and (Goods.price lessEq maxPrice) }
                .orderBy(Goods.price, if (sortDirection == SortDirection.ASC) SortOrder.ASC else SortOrder.DESC)
                .map { it.toGoods() }
        }
    }

//    fun getGoodsFiltered(
//        type: GoodsType? = null,
//        minPrice: Double? = null,
//        maxPrice: Double? = null,
//        sortDirection: SortDirection = SortDirection.ASC
//    ): List<GoodsModel> {
//        return transaction {
//            Goods.select {
//                (type?.let { Goods.type eq it.name } ?: Op.TRUE) and
//                        (minPrice?.let { Goods.price greaterEq it } ?: Op.TRUE) and
//                        (maxPrice?.let { Goods.price lessEq it } ?: Op.TRUE)
//            }
//                .orderBy(Goods.price, if (sortDirection == SortDirection.ASC) SortOrder.ASC else SortOrder.DESC)
//                .map { it.toGoods() }
//        }
//    }

}