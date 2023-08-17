package com.rofiqoff.games.data.implementation.sources.remote.response

import com.google.gson.annotations.SerializedName
import com.rofiqoff.games.data.domain.model.GameResult
import com.rofiqoff.games.utils.Constants
import com.rofiqoff.games.utils.convertAs

data class GameResultResponse(
    val id: Long?,
    val slug: String,
    val name: String,
    val released: String?,
    @SerializedName("background_image")
    val backgroundImage: String?,
    val rating: Double,
) {

    val asGameResult: GameResult
        get() = GameResult(
            id = id ?: 0,
            slug = slug,
            name = name,
            released = released.orEmpty() convertAs Constants.DATE_TIME_FORMAT,
            imageUrl = backgroundImage.orEmpty(),
            rating = rating.toString(),
        )

}
