package io.eugenethedev.taigamobile.domain.entities

import io.eugenethedev.taigamobile.dagger.DateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.time.LocalDateTime

@Serializable
data class Comment(
    val id: String,
    @JsonNames("user") val author: User,
    @JsonNames("comment") val text: String,
    @Serializable(with = DateTimeSerializer::class)
    @JsonNames("created_at") val postDateTime: LocalDateTime,
    @Serializable(with = DateTimeSerializer::class)
    @JsonNames("delete_comment_date") val deleteDate: LocalDateTime?
) {
    var canDelete: Boolean = false
}
