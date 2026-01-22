package com.practicum.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val image: String = "",
    val title: String,
    val description: String = "",
    val listIds: String = "",
    val count: Int = 0,
    val addTime: String
)
