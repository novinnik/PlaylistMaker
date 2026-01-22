package com.practicum.playlistmaker.media.playlists.domain.model

import android.net.Uri

data class Playlist(
    val id: Int = 0,
    val image: Uri? = null,
    val title: String,
    val description: String = "",
    val listIds: List<Int>,
    val count: Int = 0
)
