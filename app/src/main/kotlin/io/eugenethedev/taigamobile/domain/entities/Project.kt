package io.eugenethedev.taigamobile.domain.entities

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Project related entities
 */

@Serializable
data class Project(
    val id: Long,
    val name: String,
    val slug: String,
    @JsonNames("i_am_member") val isMember: Boolean = false,
    @JsonNames("i_am_admin") val isAdmin: Boolean = false,
    @JsonNames("i_am_owner") val isOwner: Boolean = false,
    val description: String? = null,
    @JsonNames("logo_small_url") val avatarUrl: String? = null,
    val members: List<Long> = emptyList(),
    @JsonNames("total_fans") val fansCount: Int = 0,
    @JsonNames("total_watchers") val watchersCount: Int = 0,
    @JsonNames("is_private") val isPrivate: Boolean = false
)