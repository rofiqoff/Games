package com.rofiqoff.games.data.implementation.sources.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(game: GameFavoriteEntity)

    @Query("SELECT * FROM ${DatabaseConstant.TABLE_NAME}")
    fun fetchAllFavoriteGames(): List<GameFavoriteEntity>

    @Query("SELECT * FROM ${DatabaseConstant.TABLE_NAME} WHERE id = :id limit 1")
    fun findById(id: Long): GameFavoriteEntity?

    @Query("DELETE FROM ${DatabaseConstant.TABLE_NAME} WHERE id = :id")
    fun deleteById(id: Long)

}
