package com.rofiqoff.games.data.implementation.repository

import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.data.domain.repository.GameFavoriteRepository
import com.rofiqoff.games.data.implementation.sources.local.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GameFavoriteRepositoryImpl(
    private val database: AppDatabase,
) : GameFavoriteRepository {

    override fun fetchAllFavoriteGames(): Flow<List<GameDetail>> = flow {
        emit(database.gameFavoriteDao().fetchAllFavoriteGames().map { it.asGameDetail })
    }.catch { emit(emptyList()) }.flowOn(Dispatchers.IO)

    override fun insertFavoriteGame(gameDetail: GameDetail): Flow<Boolean> = flow {
        database.gameFavoriteDao().insertGames(gameDetail.asGameEntity)
        emit(true)
    }.catch { emit(false) }.flowOn(Dispatchers.IO)

    override fun deleteFavoriteGameById(id: Long): Flow<Boolean> = flow<Boolean> {
        database.gameFavoriteDao().deleteById(id)
        emit(true)
    }.catch { emit(false) }.flowOn(Dispatchers.IO)

    override fun checkFavoriteGameById(id: Long): Flow<Boolean> = flow {
        val check = database.gameFavoriteDao().findById(id)
        emit(check != null)
    }.catch { emit(false) }.flowOn(Dispatchers.IO)

}
