package com.example.pokemonultimate.data.model.pokemonCardModel

import androidx.room.Entity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
@Serializable
data class TcgPlayerEntity(
    val url: String,
    @Serializable(DateSerializer::class)
    val updatedAt: Date,
    val prices: PriceEntity? = null,
) {
    object DateSerializer : KSerializer<Date> {
        private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)

        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Date) {
            encoder.encodeString(dateFormat.format(value))
        }

        override fun deserialize(decoder: Decoder): Date {
            return dateFormat.parse(decoder.decodeString())
                ?: throw SerializationException("Invalid date format")
        }
    }
}
