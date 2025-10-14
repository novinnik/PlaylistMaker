package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.ResultSearch

interface TracksSearchInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun interface TracksConsumer{
        fun consume(tracksSearch: ResultSearch)
    }
}