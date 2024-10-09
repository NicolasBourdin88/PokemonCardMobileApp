package com.example.pokemonultimate.data.api

import com.example.pokemonultimate.data.model.PokemonCardEntity

object ApiRepository {
    fun search(query: String, page: Int, pageSize: Int): ApiResponse<List<PokemonCardEntity>> =
        with(ApiController) {
            return callApi(
                ApiRoutes.search(query, page, pageSize),
                ApiController.ApiMethod.GET
            )
        }
}
