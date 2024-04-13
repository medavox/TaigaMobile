package io.eugenethedev.taigamobile.domain.entities

import io.eugenethedev.taigamobile.dagger.DateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.time.LocalDateTime

@Serializable
data class WikiPage(
    val id: Long,
    val version: Int,
    val content: String,
    val editions: Long,
    @JsonNames("created_date")
    @Serializable(with = DateTimeSerializer::class)
    val cratedDate: LocalDateTime,
    @JsonNames("is_watcher") val isWatcher: Boolean,
    @JsonNames("last_modifier") val lastModifier: Long,
    @Serializable(with = DateTimeSerializer::class)
    @JsonNames("modified_date") val modifiedDate: LocalDateTime,
    @JsonNames("total_watchers") val totalWatchers: Long,
    @JsonNames("slug") val slug: String
)

@Serializable
data class WikiLink(
    @JsonNames("href") val ref: String,
    val id: Long,
    val order: Long,
    val title: String
)