package com.practicum.playlistmaker.media.playlists.model

import android.net.Uri

data class FormPlaylistState(
    val uriCover: Uri? = null,
    val title: String,
    val description: String
)
