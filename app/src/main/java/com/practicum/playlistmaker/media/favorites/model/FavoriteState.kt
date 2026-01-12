package com.practicum.playlistmaker.media.favorites.model

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoriteState {
    data object Loading: FavoriteState
    data class Content(val tracks: ArrayList<Track>): FavoriteState
    data object Empty: FavoriteState
}
