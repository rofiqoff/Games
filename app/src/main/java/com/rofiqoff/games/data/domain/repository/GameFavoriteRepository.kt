package com.rofiqoff.games.data.domain.repository

import com.rofiqoff.games.data.domain.model.GameDetail
import kotlinx.coroutines.flow.Flow

interface GameFavoriteRepository {

    fun fetchAllFavoriteGames(): Flow<List<GameDetail>>

    fun insertFavoriteGame(gameDetail: GameDetail): Flow<Boolean>

    fun deleteFavoriteGameById(id: Long): Flow<Boolean>

    fun checkFavoriteGameById(id: Long): Flow<Boolean>

}
