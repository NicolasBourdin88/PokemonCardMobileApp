package com.example.pokemonultimate.data.api

import com.example.pokemonultimate.ui.utils.setFirstToUpperCase

object ApiRoutes {
    private const val API = "https://api.pokemontcg.io/v2/"

    fun search(query: String?, filters: String?, page: Int, pageSize: Int): String {
        var url = "${API}cards?q="

        if (query != null) url += "name:$query&"
        if (filters != null) url += "types:${filters.setFirstToUpperCase()}&"
        url += "orderBy=number&page=$page&" +
                "pageSize=$pageSize"

        return url
    }
}
