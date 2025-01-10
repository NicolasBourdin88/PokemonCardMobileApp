package com.example.pokemonultimate.data.api

import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.sets.Set
import com.example.pokemonultimate.ui.screens.home.FilterCategories

object ApiRepository {
    fun getSearch(
        query: String?,
        filtersTypes: List<String>,
        filtersSubTypes: List<String>,
        filtersSuperTypes: List<String>,
        setId: String? = null,
        page: Int,
        pageSize: Int
    ): ApiResponse<List<PokemonCardEntity>> {
        with(ApiController) {
            return callApi(
                ApiRoutes.search(
                    query,
                    filtersTypes,
                    filtersSubTypes,
                    filtersSuperTypes,
                    page,
                    pageSize,
                    setId,
                ),
                ApiController.ApiMethod.GET
            )
        }
    }

    fun getCardsFromSet(
        setId: String,
        page: Int,
        pageSize: Int
    ): ApiResponse<List<PokemonCardEntity>> {
        return ApiController.callApi(
            ApiRoutes.set(setId, page, pageSize),
            ApiController.ApiMethod.GET
        )
    }

    fun getFilters(filterCategories: FilterCategories): ApiResponse<List<String>> {
        return ApiController.callApi(
            ApiRoutes.filters(filterCategories),
            method = ApiController.ApiMethod.GET
        )
    }

    fun getSets(): ApiResponse<List<Set>> {
        return ApiController.callApi(ApiRoutes.sets(), method = ApiController.ApiMethod.GET)
    }
}
