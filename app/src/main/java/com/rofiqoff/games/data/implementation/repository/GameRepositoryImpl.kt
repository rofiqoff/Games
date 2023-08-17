package com.rofiqoff.games.data.implementation.repository

import com.rofiqoff.games.configuration.network.AppResponse
import com.rofiqoff.games.configuration.network.NetworkHelper
import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.data.domain.model.Games
import com.rofiqoff.games.data.domain.repository.GameRepository
import com.rofiqoff.games.data.implementation.sources.remote.api.ApiService
import kotlinx.coroutines.flow.Flow

class GameRepositoryImpl(
    private val api: ApiService,
) : GameRepository {

    override fun getGamesByDynamicUrl(url: String): Flow<AppResponse<Games>> {
        return NetworkHelper.createRequest { api.getGamesByDynamicUrl(url).asGames }
    }

    override fun getAllGames(page: Int): Flow<AppResponse<Games>> {
        return NetworkHelper.createRequest { api.getAllGames(page).asGames }
    }

    override fun searchGame(query: String): Flow<AppResponse<Games>> {
        return NetworkHelper.createRequest { api.searchGames(query).asGames }
    }

    override fun getGameDetail(slug: String): Flow<AppResponse<GameDetail>> {
        return NetworkHelper.createRequest { api.getGameDetail(slug).asGameDetail }
    }

}
