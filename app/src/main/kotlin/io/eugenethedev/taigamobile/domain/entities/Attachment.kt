package io.eugenethedev.taigamobile.domain.entities

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Attachment(
    val id: Long,
    val name: String,
    @JsonNames("size") val sizeInBytes: Long,
    val url: String
)
