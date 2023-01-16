package ru.nimble.database.goods

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Goods : Table() {
    private val idgoods = Goods.varchar("idgoods", 100)
    private val name = Goods.varchar("name", 100)
    private val manufacturer = Goods.varchar("manufacturer", 50)
    private val logo = Goods.varchar("logo", 0)
    private val grade = Goods.integer("grade")
    private val description = Goods.varchar("description", 500)
    private val specification = Goods.varchar("specification", 500)
    private val availability = Goods.integer("availability")


    fun insert(GoodsDTO: GoodsDTO){
        transaction {
            Goods.insert(){
                it[idgoods] = GoodsDTO.idgoods
                it[name] = GoodsDTO.name
                it[manufacturer] = GoodsDTO.manufacturer
                it[logo] = GoodsDTO.logo
                it[grade] = GoodsDTO.grade
                it[description] = GoodsDTO.description
                it[specification] = GoodsDTO.specification
                it[availability] = GoodsDTO.availability
            }
        }
    }

    fun fetchGoods(): List<GoodsDTO> {
        return try {
            transaction {
                Goods.selectAll().toList()
                    .map {
                        GoodsDTO(
                            idgoods = it[idgoods],
                            name = it[name],
                            manufacturer = it[manufacturer],
                            logo = it[logo],
                            grade = it[grade],
                            description = it[description],
                            specification = it[specification],
                            availability = it[availability]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}