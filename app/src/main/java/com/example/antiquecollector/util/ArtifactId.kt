package com.example.antiquecollector.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull

/**
 * Represents an identifier for an artifact, which can be either local or remote.
 */
@Serializable(with = ArtifactIdSerializer::class)
sealed class ArtifactId {

    /**
     * Local artifact ID with numeric identifier
     */
    data class Local(val id: Long) : ArtifactId()

    /**
     * Remote artifact ID with string identifier
     */
    data class Remote(val id: String) : ArtifactId()

    companion object {
        // Create a standard JSON configuration
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }

        /**
         * Deserialize from a JSON string to ArtifactId
         */
        fun deserialize(serialized: String): ArtifactId? {
            return try {
                json.decodeFromString(ArtifactIdSerializer, serialized)
            } catch (e: Exception) {
                null
            }
        }

        /**
         * Serialize ArtifactId to a JSON string
         */
        fun serialize(artifactId: ArtifactId): String {
            return json.encodeToString(ArtifactIdSerializer, artifactId)
        }
    }
}

/**
 * Custom serializer for ArtifactId that handles the polymorphic serialization
 */
object ArtifactIdSerializer : KSerializer<ArtifactId> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ArtifactId") {
        element<String>("type")
        element<JsonElement>("value")
    }

    override fun serialize(encoder: Encoder, value: ArtifactId) {
        val jsonEncoder = encoder as? JsonEncoder ?: throw IllegalArgumentException("This serializer can only be used with JSON")

        val jsonObject = when (value) {
            is ArtifactId.Local -> JsonObject(
                mapOf(
                    "type" to JsonPrimitive("Local"),
                    "id" to JsonPrimitive(value.id)
                )
            )
            is ArtifactId.Remote -> JsonObject(
                mapOf(
                    "type" to JsonPrimitive("Remote"),
                    "id" to JsonPrimitive(value.id)
                )
            )
        }

        jsonEncoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): ArtifactId {
        val jsonDecoder = decoder as? JsonDecoder ?: throw IllegalArgumentException("This serializer can only be used with JSON")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        return when (val type = jsonObject["type"]?.jsonPrimitive?.content) {
            "Local" -> {
                val id = jsonObject["id"]?.jsonPrimitive?.longOrNull
                    ?: throw IllegalArgumentException("Local ID must be a number")
                ArtifactId.Local(id)
            }
            "Remote" -> {
                val id = jsonObject["id"]?.jsonPrimitive?.content
                    ?: throw IllegalArgumentException("Remote ID must be a string")
                ArtifactId.Remote(id)
            }
            null -> {
                // Fallback for JSONs without type
                if (jsonObject.containsKey("id")) {
                    val idElement = jsonObject["id"]!!
                    if (idElement is JsonPrimitive) {
                        if (idElement.jsonPrimitive.longOrNull != null) {
                            ArtifactId.Local(idElement.jsonPrimitive.long)
                        } else {
                            ArtifactId.Remote(idElement.jsonPrimitive.content)
                        }
                    } else {
                        throw IllegalArgumentException("Invalid format for ArtifactId")
                    }
                } else {
                    throw IllegalArgumentException("Missing id field in ArtifactId")
                }
            }
            else -> throw IllegalArgumentException("Unknown ArtifactId type: $type")
        }
    }
}