package com.example.pokemonultimate.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ApiResponseStatus {

    @SerialName("error")
    ERROR,

    @SerialName("success")
    SUCCESS,

    @SerialName("unknown")
    UNKNOWN;
}
