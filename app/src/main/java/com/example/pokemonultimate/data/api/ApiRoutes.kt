package com.example.pokemonultimate.data.api

object ApiRoutes {
    private const val API = "https://api.pokemontcg.io/v2/"

    fun search(query: String, page: Int, pageSize: Int): String {
        return "${API}cards?q=name:$query&orderBy=number&page=$page&pageSize=$pageSize"
    }
}
