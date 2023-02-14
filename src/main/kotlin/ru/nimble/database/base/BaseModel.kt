package ru.nimble.database.base

import kotlinx.serialization.Serializable

@Serializable
abstract class BaseModel : IdentifiedBase<String>()

