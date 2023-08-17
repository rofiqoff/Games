package com.rofiqoff.games.data.implementation.sources.remote.response

import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.utils.Constants
import com.rofiqoff.games.utils.convertAs

data class GameDetailResponse(
    val name: String?,
    val description: String?,
    val released: String?,
    val backgroundImage: String?,
    val website: String?,
    val rating: Double,
) {
    val asGameDetail: GameDetail
        get() = GameDetail(
            name = name.orEmpty(),
            description = description.orEmpty(),
            released = released.orEmpty() convertAs Constants.DATE_TIME_FORMAT,
            backgroundImage = backgroundImage.orEmpty(),
            website = website.orEmpty(),
            rating = rating.toString(),
        )
}