package com.rofiqoff.games.data.domain.model

data class Games(
    val next: String,
    val previous: String,
    val results: List<GameResult>,
)
