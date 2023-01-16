package ru.nimble.database.goods

import kotlinx.serialization.Serializable
import ru.nimble.features.login.goods.models.CreateGoodsRequest
import ru.nimble.features.login.goods.models.CreateGoodsResponse
import java.util.UUID

@Serializable
class GoodsDTO (
    val idgoods : String,
    val name : String,
    val manufacturer : String,
    val logo : String,
    val grade : Int,
    val description : String,
    val specification : String,
    val availability : Int,
)

fun CreateGoodsRequest.mapToGoodsDTO(): GoodsDTO =
    GoodsDTO(
        idgoods = UUID.randomUUID().toString(),
        name = name,
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
        manufacturer = manufacturer,
        logo = logo,
        grade = grade,
        description = description,
        specification = specification,
        availability = availability
    )