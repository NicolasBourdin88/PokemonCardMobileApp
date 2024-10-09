package com.example.pokemonultimate.data.api

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
class ApiError(
    val code: String? = null,
    val description: String? = null,
    @Contextual
    @SerialName("context")
    var contextJson: JsonObject? = null,
    val errors: Array<ApiError>? = null,
    @Contextual
    val exception: Exception? = null
)
