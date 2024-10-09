package com.example.pokemonultimate.data.api

import com.example.pokemonultimate.data.model.pokemonCardModel.PokemonCardEntity

object ApiRepository {
    fun search(
        query: String?,
        filters: String?,
        page: Int,
        pageSize: Int
    ): ApiResponse<List<PokemonCardEntity>> =
        with(ApiController) {
            return callApi(
                ApiRoutes.search(query, filters, page, pageSize),
                ApiController.ApiMethod.GET
            )
        }
}
