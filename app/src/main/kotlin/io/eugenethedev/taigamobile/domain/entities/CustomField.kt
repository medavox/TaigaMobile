package io.eugenethedev.taigamobile.domain.entities

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.time.LocalDate

@Serializable
enum class CustomFieldType {
    @JsonNames("text") Text,
    @JsonNames("multiline") Multiline,
    @JsonNames("richtext") RichText,
    @JsonNames("date") Date,
    @JsonNames("url") Url,
    @JsonNames("dropdown") Dropdown,
    @JsonNames("number") Number,
    @JsonNames("checkbox") Checkbox
}

data class CustomField(
    val id: Long,
    val type: CustomFieldType,
    val name: String,
    val description: String?,
    val value: CustomFieldValue?,
    val options: List<String>? = null // for CustomFieldType.Dropdown
)

@JvmInline
value class CustomFieldValue(val value: Any) {
    init {
        require(
            value is String ||
            value is LocalDate ||
            value is Double ||
            value is Boolean
        )
    }

    val stringValue get() = value as? String ?: throw IllegalArgumentException("value is not String")
    val doubleValue get() = value as? Double ?: throw IllegalArgumentException("value is not Int")
    val dateValue get() = value as? LocalDate ?: throw IllegalArgumentException("value is not Date")
    val booleanValue get() = value as? Boolean ?: throw IllegalArgumentException("value is not Boolean")
}


data class CustomFields(
    val fields: List<CustomField>,
    val version: Int
)
