package com.rofiqoff.games.data.implementation.sources.remote.response

import com.rofiqoff.games.data.domain.model.Games

data class GamesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GameResultResponse>,
) {
    val asGames: Games
        get() = Games(
            next = next.orEmpty(),
            previous = previous.orEmpty(),
            results = results.map { it.asGameResult },
        )
}
