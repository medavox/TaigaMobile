package io.eugenethedev.taigamobile.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Swimlane(
    val id: Long,
    val name: String,
    val order: Long
)
