package io.eugenethedev.taigamobile.dagger

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.*
import java.time.format.DateTimeFormatter


@Serializer(forClass = LocalDate::class)
object DateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(formatter.format(value))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}

@Serializer(forClass = LocalDate::class)
object DateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.atZone(ZoneId.systemDefault())
            .toInstant()
            .toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return Instant.parse(decoder.decodeString())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}

// used in TaskRepository
fun String.toLocalDate(): LocalDate = LocalDate.parse(this)

