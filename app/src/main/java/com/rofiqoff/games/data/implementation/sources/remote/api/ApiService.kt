package com.rofiqoff.games.data.implementation.sources.remote.api

import com.rofiqoff.games.data.implementation.sources.remote.response.GameDetailResponse
import com.rofiqoff.games.data.implementation.sources.remote.response.GamesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun getGamesByDynamicUrl(@Url url: String): GamesResponse

    @GET("games")
    suspend fun getAllGames(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10,
    ): GamesResponse

    @GET("games")
    suspend fun searchGames(
        @Query("search") query: String,
    ): GamesResponse

    @GET("games/{slug}")
    suspend fun getGameDetail(@Path("slug") slug: String): GameDetailResponse
}
