package com.practicum.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorites_table")
data class FavoritesEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String? = "",
    val artistName: String? = "",
    val trackTime: Long? = 0L,
    val albumPoster: String? = "",
    val collectionName: String? = "",
    val releaseDate: String? = "",
    val primaryGenreName: String? = "",
    val country: String? = "",
    val previewUrl: String? = "",
    val addTime: String
)
