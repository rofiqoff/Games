package com.rofiqoff.games.data.domain.repository

import androidx.paging.PagingData
import com.rofiqoff.games.configuration.network.AppResponse
import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.data.domain.model.GameResult
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getAllGames(): Flow<PagingData<GameResult>>

    fun searchGame(query: String): Flow<PagingData<GameResult>>

    fun getGameDetail(slug: String): Flow<AppResponse<GameDetail>>

}
