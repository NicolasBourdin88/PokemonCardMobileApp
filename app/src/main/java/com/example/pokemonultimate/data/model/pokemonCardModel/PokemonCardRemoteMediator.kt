package com.example.pokemonultimate.data.model.pokemonCardModel

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import com.example.pokemonultimate.data.api.ApiRepository
import com.example.pokemonultimate.data.model.pokemonCardModel.database.PokemonCardDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonCardRemoteMediator(
    private val pokemonCardDb: PokemonCardDataBase,
    val query: String?,
    val filters: String?,
) : RemoteMediator<Int, PokemonCardEntity>() {
    private var page = 1
    private var shouldGoNextPage: Boolean? = null

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonCardEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    page = 1
                    1
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    if (shouldGoNextPage == false) {
                        MediatorResult.Success(endOfPaginationReached = true)
                    }
                    page++
                }
            }

            withContext(Dispatchers.IO) {
                val apiResponse = ApiRepository.search(
                    page = loadKey,
                    pageSize = state.config.pageSize,
                    query = query,
                    filters = filters,
                )

                shouldGoNextPage = apiResponse.totalCount < 10
                val pokemonCards = apiResponse.data

                pokemonCardDb.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        pokemonCardDb.dao.clearAll()
                        pokemonCardDb.remoteKeyDao.clearRemoteKeys()
                    }
                    pokemonCards?.let {
                        pokemonCardDb.dao.upsertAll(it)
                        val keys = pokemonCards.map { card ->
                            RemoteKey(
                                id = card.id,
                                nextKey = loadKey + 1
                            )
                        }
                        pokemonCardDb.remoteKeyDao.insertAll(keys)
                    }
                }

                MediatorResult.Success(
                    endOfPaginationReached = pokemonCards.isNullOrEmpty()
                )
            }
        } catch (e: IOException) {
            Log.e("RemoteMediator", "IOException: ${e.message}")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e("RemoteMediator", "HttpException: ${e.message}")
            MediatorResult.Error(e)
        }
    }
}
