package io.eugenethedev.taigamobile.domain.entities

import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Tasks related entities
 */

data class Status(
    val id: Long,
    val name: String,
    val color: String,
    val type: StatusType
)

enum class StatusType {
    Status,
    Type,
    Severity,
    Priority
}

enum class CommonTaskType {
    UserStory,
    Task,
    Epic,
    Issue
}


data class CommonTask(
    val id: Long,
    val createdDate: LocalDateTime,
    val title: String,
    val ref: Int,
    val status: Status,
    val assignee: User? = null,
    val projectInfo: Project,
    val taskType: CommonTaskType,
    val isClosed: Boolean,
    val tags: List<Tag> = emptyList(),
    val colors: List<String> = emptyList(), // colored indicators (for stories and epics)
    val blockedNote: String? = null
)


@Serializable
enum class DueDateStatus {
    @JsonNames("not_set") NotSet,
    @JsonNames("set") Set,
    @JsonNames("due_soon") DueSoon,
    @JsonNames("past_due") PastDue,
    @JsonNames("no_longer_applicable") NoLongerApplicable
}

data class CommonTaskExtended(
    val id: Long,
    val status: Status,
    val taskType: CommonTaskType,
    val createdDateTime: LocalDateTime,
    val sprint: Sprint?,
    val assignedIds: List<Long>,
    val watcherIds: List<Long>,
    val creatorId: Long,
    val ref: Int,
    val title: String,
    val isClosed: Boolean,
    val description: String,
    val projectSlug: String,
    val version: Int,
    val epicsShortInfo: List<EpicShortInfo> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val swimlane: Swimlane?,
    val dueDate: LocalDate?,
    val dueDateStatus: DueDateStatus?,
    val userStoryShortInfo: UserStoryShortInfo? = null,
    val url: String,
    val blockedNote: String? = null,

    // for epic
    val color: String? = null,

    // for issue
    val type: Status? = null,
    val priority: Status? = null,
    val severity: Status? = null
)

@Serializable
data class EpicShortInfo(
    val id: Long,
    @JsonNames("subject") val title: String,
    val ref: Int,
    val color: String
)

@Serializable
data class UserStoryShortInfo(
    val id: Long,
    val ref: Int,
    @JsonNames("subject") val title: String,
    val epics: List<EpicShortInfo>?
) {
    val epicColors get() = epics?.map { it.color }.orEmpty()
}
