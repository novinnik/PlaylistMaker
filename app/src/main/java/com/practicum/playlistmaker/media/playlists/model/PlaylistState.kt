package com.practicum.playlistmaker.media.playlists.model

import com.practicum.playlistmaker.media.playlists.domain.model.Playlist

sealed interface PlaylistState {
    data object Loading: PlaylistState
    data class Content(val playlist: List<Playlist>): PlaylistState
    data object Empty: PlaylistState
}