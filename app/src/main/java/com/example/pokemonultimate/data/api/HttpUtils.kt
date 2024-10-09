package com.example.pokemonultimate.data.api

import com.example.pokemonultimate.BuildConfig.API_KEY
import okhttp3.Headers

object HttpUtils {

    fun getHeaders(contentType: String? = "application/json; charset=UTF-8"): Headers {
        return Headers.Builder().apply {
            add("Authorization", API_KEY)
        }.run {
            build()
        }
    }
}
