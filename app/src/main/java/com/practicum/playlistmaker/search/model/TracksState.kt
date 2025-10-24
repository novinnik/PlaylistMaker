package com.practicum.playlistmaker.search.model

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface TracksState {

    object Loading: TracksState

    data class Content(val tracks: ArrayList<Track>): TracksState

    data class Error(val errorCode: Int): TracksState

    data class Empty(val isEmpty: Boolean): TracksState
}