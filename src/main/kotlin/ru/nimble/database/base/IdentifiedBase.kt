package ru.nimble.database.base

import kotlinx.serialization.Serializable

@Serializable
abstract class IdentifiedBase<T> {
    abstract val id: T
}