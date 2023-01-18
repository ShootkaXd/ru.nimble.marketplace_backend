package ru.nimble.database.goods

import kotlinx.serialization.Serializable
import ru.nimble.features.login.goods.models.CreateGoodsRequest
import ru.nimble.features.login.goods.models.CreateGoodsResponse
import ru.nimble.features.login.goods.models.GoodsResponse
import java.util.UUID

@Serializable
class GoodsDTO (
    val idgoods : String,
    val name : String,
    val price: Double,
    val manufacturer : String,
    val logo : String,
    val grade : Double,
    val description : String,
    val specification : String,
    val availability : Int,
)

fun CreateGoodsRequest.mapToGoodsDTO(): GoodsDTO =
    GoodsDTO(
        idgoods = UUID.randomUUID().toString(),
        name = name,
        price = price,
        manufacturer = manufacturer,
        logo = logo,
        grade = grade,
        description = description,
        specification = specification,
        availability = availability
    )

fun GoodsDTO.mapToCreateGoodsResponse(): CreateGoodsResponse =
    CreateGoodsResponse(
        idgoods = idgoods,
        name = name,
        price = price,
        manufacturer = manufacturer,
        logo = logo,
        grade = grade,
        description = description,
        specification = specification,
        availability = availability
    )


fun GoodsDTO.mapToGoodsResponse(): GoodsResponse =
    GoodsResponse(
        idgoods = idgoods,
        name = name,
        price = price,
        manufacturer = manufacturer,
        logo = logo,
        grade = grade,
        description = description,
        specification = specification,
        availability = availability
    )