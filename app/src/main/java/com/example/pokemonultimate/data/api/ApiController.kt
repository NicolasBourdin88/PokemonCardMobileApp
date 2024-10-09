package com.example.pokemonultimate.data.api

import android.util.Log
import androidx.annotation.StringRes
import com.example.pokemonultimate.R
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.UnknownHostException

object ApiController {
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    inline fun <reified T> callApi(
        url: String,
        method: ApiMethod,
        body: Any? = null,
        okHttpClient: OkHttpClient = OkHttpClient.Builder().build(),
        noinline buildErrorResult: ((apiError: ApiError?, translatedErrorRes: Int) -> T)? = null,
    ): T {
        val requestBody: RequestBody = generateRequestBody(body)
        return executeRequest(
            url,
            method,
            requestBody,
            okHttpClient,
            buildErrorResult
        )
    }

    fun generateRequestBody(body: Any?): RequestBody {
        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val toJson = when (body) {
            is String -> body
            else -> body.toString()
        }
        return toJson.toRequestBody(jsonMediaType)
    }

    inline fun <reified T> executeRequest(
        url: String,
        method: ApiMethod,
        requestBody: RequestBody,
        okHttpClient: OkHttpClient,
        noinline buildErrorResult: ((apiError: ApiError?, translatedErrorRes: Int) -> T)?,
    ): T {
        var bodyResponse = ""
        try {
            val request = Request.Builder()
                .url(url)
                .headers(HttpUtils.getHeaders())
                .apply {
                    when (method) {
                        ApiMethod.GET -> get()
                        ApiMethod.POST -> post(requestBody)
                        ApiMethod.DELETE -> delete(requestBody)
                        ApiMethod.PUT -> put(requestBody)
                        ApiMethod.PATCH -> patch(requestBody)
                    }
                }
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                bodyResponse = response.body?.string() ?: ""
                return when {
                    response.code >= 500 -> {
                        createErrorResponse(
                            apiError = createApiError(
                                true,
                                bodyResponse,
                                ServerErrorException()
                            ),
                            translatedError = R.string.app_name,
                            buildErrorResult = buildErrorResult,
                        )
                    }

                    bodyResponse.isBlank() -> createInternetErrorResponse(buildErrorResult = buildErrorResult)
                    else -> createApiResponse<T>(bodyResponse)
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()

            return if (exception.isNetworkException()) {
                createInternetErrorResponse(
                    noNetwork = exception is UnknownHostException,
                    buildErrorResult = buildErrorResult
                )
            } else {
                createErrorResponse(
                    translatedError = R.string.app_name,
                    apiError = createApiError(
                        true,
                        bodyResponse,
                        exception = exception
                    ),
                    buildErrorResult = buildErrorResult,
                )
            }

        }
    }

    inline fun <reified T> createApiResponse(
        bodyResponse: String
    ): T {
        val apiResponse = json.decodeFromString<T>(bodyResponse)

        if (apiResponse is ApiResponse<*> && apiResponse.result == ApiResponseStatus.ERROR) {
            apiResponse.translatedError = R.string.app_name
        }

        return apiResponse
    }

    private fun String.bodyResponseToJson(): JsonObject {
        return JsonObject(mapOf("bodyResponse" to JsonPrimitive(this)))
    }

    inline fun <reified T> createInternetErrorResponse(
        noNetwork: Boolean = false,
        noinline buildErrorResult: ((apiError: ApiError?, translatedErrorRes: Int) -> T)?,
    ) = createErrorResponse<T>(
        translatedError = if (noNetwork) R.string.app_name else R.string.app_name,
        apiError = ApiError(exception = NetworkException()),
        buildErrorResult = buildErrorResult,
    )

    fun createApiError(
        useKotlinxSerialization: Boolean,
        bodyResponse: String,
        exception: Exception
    ) = ApiError(
        contextJson = if (useKotlinxSerialization) bodyResponse.bodyResponseToJson() else null,
        exception = exception
    )

    inline fun <reified T> createErrorResponse(
        @StringRes translatedError: Int,
        apiError: ApiError? = null,
        noinline buildErrorResult: ((apiError: ApiError?, translatedErrorRes: Int) -> T)?,
    ): T {
        return buildErrorResult?.invoke(apiError, translatedError)
            ?: ApiResponse<Any>(
                result = ApiResponseStatus.ERROR,
                error = apiError,
                translatedError = translatedError,
                totalCount = -1,
            ) as T
    }

    class NetworkException : Exception()
    class ServerErrorException : Exception()

    enum class ApiMethod {
        GET, PUT, POST, DELETE, PATCH
    }

    fun Exception.isNetworkException(): Boolean {
        val okHttpException = arrayOf("stream closed", "required settings preface not received")
        return this.javaClass.name.contains("java.net.", ignoreCase = true) ||
                this.javaClass.name.contains("javax.net.", ignoreCase = true) ||
                this is java.io.InterruptedIOException ||
                this is okhttp3.internal.http2.StreamResetException ||
                (this is java.io.IOException && this.message?.lowercase() in okHttpException)
    }
}
