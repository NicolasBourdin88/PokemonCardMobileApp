package com.example.pokemonultimate.data.api

import com.example.pokemonultimate.ui.screens.home.FilterCategories

object ApiRoutes {
    private const val API = "https://api.pokemontcg.io/v2/"

    fun search(
        query: String?,
        filtersTypes: List<String>,
        filtersSubTypes: List<String>,
        filtersSuperTypes: List<String>,
        page: Int,
        pageSize: Int
    ): String {
        var url = "${API}cards?q=("
        val baseUrl = url

        if (!query.isNullOrEmpty()) url += "name:$query*"
        url += filtersTypes.addFilters(FilterCategories.TYPES, url == baseUrl)
        url += filtersSubTypes.addFilters(FilterCategories.SUBTYPES, url == baseUrl)
        url += filtersSuperTypes.addFilters(FilterCategories.SUPERTYPE, url == baseUrl)
        url += ")&orderBy=number&page=$page&pageSize=$pageSize"

        return url
    }

    private fun List<String>.addFilters(filters: FilterCategories, isFirst: Boolean): String {
        if (isNotEmpty()) {
            var returnedValue = ""
            forEachIndexed { position, filterName ->
                returnedValue += if (isFirst && position == 0) {
                    "${filters.name.lowercase()}:"
                } else {
                    " AND ${filters.name.lowercase()}:"
                }
                returnedValue += "\"$filterName\""
            }
            return returnedValue
        } else {
            return ""
        }
    }

    fun filters(filters: FilterCategories): String {
        val filter = filters.name.lowercase().let {
            if (it.last() != 's') it + 's' else it
        }
        return "${API}$filter"
    }
}
