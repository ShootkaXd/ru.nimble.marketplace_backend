package ru.nimble.database.goods

import kotlinx.serialization.Serializable
import ru.nimble.database.base.BaseModel
import ru.nimble.features.login.goods.models.*

@Serializable
class GoodsModel(
    override val id: String,
    val name : String,
    val price: Double,
    val manufacturer : String,
    val logo : String,
    val grade : Double,
    val description : String,
    val specification : String,
    val availability : Int,
): BaseModel()

fun GoodsRequest.toGoodsModel(): GoodsModel =
    GoodsModel(
        id = "",
        name = name,
        price = price,
        manufacturer = manufacturer,
        logo = logo,
        grade = grade,
        description = description,
        specification = specification,
        availability = availability
    )

fun GoodsModel.toGoodsResponse(): GoodsResponse =
    GoodsResponse(
        id = id,
        name = name,
        price = price,
        manufacturer = manufacturer,
        logo = logo,
        grade = grade,
        description = description,
        specification = specification,
        availability = availability
    )
