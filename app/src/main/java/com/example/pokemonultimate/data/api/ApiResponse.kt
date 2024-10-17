package com.example.pokemonultimate.data.api

import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class ApiResponse<T>(
    val result: ApiResponseStatus = ApiResponseStatus.UNKNOWN,
    val data: @RawValue T? = null,
    val error: ApiError? = null,
    val page: Int = 0,
    val totalCount: Int = -1,
    @SerialName("response_at")
    val responseAt: Long = 0,
    val total: Int = 0,
    var translatedError: Int = 0,
    @SerialName("items_per_page")
    val itemsPerPage: Int = 0,
) {

    fun isSuccess() = result == ApiResponseStatus.SUCCESS
}
