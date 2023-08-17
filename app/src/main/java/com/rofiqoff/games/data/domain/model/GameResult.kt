package com.rofiqoff.games.data.domain.model

data class GameResult(
    val id: Long,
    val slug: String,
    val name: String,
    val released: String,
    val imageUrl: String,
    val rating: String,
)
