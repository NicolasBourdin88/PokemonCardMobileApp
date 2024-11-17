package com.example.pokemonultimate.data.api

import kotlinx.parcelize.RawValue
import kotlinx.serialization.Serializable

@Serializable
open class ApiResponse<T>(
    val result: ApiResponseStatus = ApiResponseStatus.UNKNOWN,
    val data: @RawValue T? = null,
    val error: ApiError? = null,
    var translatedError: Int = 0,
    val page: Int = 0,
    val count: Int = 0,
    val totalCount: Int = -1,
)
