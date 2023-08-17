package com.rofiqoff.games.data.domain.repository

import com.rofiqoff.games.configuration.network.AppResponse
import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.data.domain.model.Games
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getGamesByDynamicUrl(url: String): Flow<AppResponse<Games>>

    fun getAllGames(page: Int): Flow<AppResponse<Games>>

    fun searchGame(query: String): Flow<AppResponse<Games>>

    fun getGameDetail(slug: String): Flow<AppResponse<GameDetail>>

}
