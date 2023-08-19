package com.rofiqoff.games.data.domain.model

import com.rofiqoff.games.data.implementation.sources.local.database.GameFavoriteEntity

data class GameDetail(
    val id: Long = 0,
    val slug: String = "",
    val name: String = "",
    val description: String = "",
    val released: String = "",
    val imageUrl: String = "",
    val website: String = "",
    val rating: String = "",
) {
    val asGameEntity: GameFavoriteEntity
        get() = GameFavoriteEntity(
            id = id,
            slug = slug,
            name = name,
            description = description,
            released = released,
            imageUrl = imageUrl,
            rating = rating,
            website = website
        )

    val asGameResult: GameResult
        get() = GameResult(
            id = id,
            slug = "",
            name = name,
            released = released,
            imageUrl = imageUrl,
            rating = rating,
        )
}
