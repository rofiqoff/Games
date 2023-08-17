package com.rofiqoff.games.data.implementation.sources.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rofiqoff.games.data.domain.model.GameDetail

@Entity(tableName = DatabaseConstant.TABLE_NAME)
data class GameFavoriteEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val description: String,
    val released: String,
    @ColumnInfo("image_url")
    val imageUrl: String,
    val website: String,
    val rating: String,
) {

    val asGameDetail: GameDetail
        get() = GameDetail(
            id = id,
            name = name,
            description = description,
            released = released,
            imageUrl = imageUrl,
            website = website,
            rating = rating,
        )

}
