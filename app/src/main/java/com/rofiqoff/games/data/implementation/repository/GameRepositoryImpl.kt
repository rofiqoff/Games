package com.rofiqoff.games.data.implementation.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rofiqoff.games.configuration.network.AppResponse
import com.rofiqoff.games.configuration.network.NetworkHelper
import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.data.domain.model.GameResult
import com.rofiqoff.games.data.domain.repository.GameRepository
import com.rofiqoff.games.data.implementation.sources.pagingsource.GamePagingSource
import com.rofiqoff.games.data.implementation.sources.remote.api.ApiService
import kotlinx.coroutines.flow.Flow

class GameRepositoryImpl(
    private val api: ApiService,
) : GameRepository {

    override fun getAllGames(): Flow<PagingData<GameResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                GamePagingSource(api)
            }
        ).flow
    }

    override fun searchGame(query: String): Flow<PagingData<GameResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                GamePagingSource(api, query)
            }
        ).flow
    }

    override fun getGameDetail(slug: String): Flow<AppResponse<GameDetail>> {
        return NetworkHelper.createRequest { api.getGameDetail(slug).asGameDetail }
    }

}
