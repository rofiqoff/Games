package com.rofiqoff.games.data.domain.model

import com.rofiqoff.games.data.implementation.sources.local.database.GameFavoriteEntity

data class GameDetail(
    val id: Long,
    val name: String,
    val description: String,
    val released: String,
    val imageUrl: String,
    val website: String,
    val rating: String,
    var isFavorite: Boolean = false,
) {
    val asGameEntity: GameFavoriteEntity
        get() = GameFavoriteEntity(
            id = id,
            name = name,
            description = description,
            released = released,
            imageUrl = imageUrl,
            rating = rating,
            website = website
        )
}
