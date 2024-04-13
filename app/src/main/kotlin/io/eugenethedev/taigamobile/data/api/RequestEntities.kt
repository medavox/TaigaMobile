package io.eugenethedev.taigamobile.data.api

import io.eugenethedev.taigamobile.dagger.DateSerializer
import io.eugenethedev.taigamobile.dagger.DateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class AuthRequest(
    val password: String,
    val username: String,
    val type: String
)

@Serializable
data class RefreshTokenRequest(
    val refresh: String
)

@Serializable
data class EditCommonTaskRequest(
    val subject: String,
    val description: String,
    val status: Long,
    val type: Long?,
    val severity: Long?,
    val priority: Long?,
    val milestone: Long?,
    val assigned_to: Long?,
    val assigned_users: List<Long>,
    val watchers: List<Long>,
    val swimlane: Long?,
    @Serializable(with = DateSerializer::class)
    val due_date: LocalDate?,
    val color: String?,
    val tags: List<List<String>>,
    val blocked_note: String,
    val is_blocked: Boolean,
    val version: Int
)

@Serializable
data class CreateCommentRequest(
    val comment: String,
    val version: Int
)

@Serializable
data class CreateCommonTaskRequest(
    val project: Long,
    val subject: String,
    val description: String,
    val status: Long?
)

@Serializable
data class CreateTaskRequest(
    val project: Long,
    val subject: String,
    val description: String,
    val milestone: Long?,
    val user_story: Long?
)

@Serializable
data class CreateIssueRequest(
    val project: Long,
    val subject: String,
    val description: String,
    val milestone: Long?,
)

@Serializable
data class CreateUserStoryRequest(
    val project: Long,
    val subject: String,
    val description: String,
    val status: Long?,
    val swimlane: Long?
)

@Serializable
data class LinkToEpicRequest(
    val epic: String,
    val user_story: Long
)

@Serializable
data class PromoteToUserStoryRequest(
    val project_id: Long
)

@Serializable
data class EditCustomAttributesValuesRequest(
    val attributes_values: Map<Long, Any?>,
    val version: Int
)

@Serializable
data class CreateSprintRequest(
    val name: String,
    @Serializable(with = DateSerializer::class)
    val estimated_start: LocalDate,
    @Serializable(with = DateSerializer::class)
    val estimated_finish: LocalDate,
    val project: Long
)

@Serializable
data class EditSprintRequest(
    val name: String,
    @Serializable(with = DateSerializer::class)
    val estimated_start: LocalDate,
    @Serializable(with = DateSerializer::class)
    val estimated_finish: LocalDate,
)

@Serializable
data class EditWikiPageRequest(
    val content: String,
    val version: Int
)

@Serializable
data class NewWikiLinkRequest(
    val href: String,
    val project: Long,
    val title: String
)
