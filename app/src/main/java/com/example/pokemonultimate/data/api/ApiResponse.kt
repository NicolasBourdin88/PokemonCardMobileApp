/*
 * Infomaniak Core - Android
 * Copyright (C) 2022-2024 Infomaniak Network SA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.example.pokemonultimate.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.RawValue

@Serializable
open class ApiResponse<T>(
    val result: ApiResponseStatus = ApiResponseStatus.UNKNOWN,
    val data: @RawValue T? = null,
    val error: ApiError? = null,
    val page: Int = 0,
    val totalCount: Int,
    @SerialName("response_at")
    val responseAt: Long = 0,
    val total: Int = 0,
    var translatedError: Int = 0,
    @SerialName("items_per_page")
    val itemsPerPage: Int = 0,
) {

    fun isSuccess() = result == ApiResponseStatus.SUCCESS
}
