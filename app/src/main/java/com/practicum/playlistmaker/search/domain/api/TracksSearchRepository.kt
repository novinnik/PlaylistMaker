package com.practicum.playlistmaker.search.domain.api


import com.practicum.playlistmaker.search.domain.models.ResultSearch

interface TracksSearchRepository {
    fun searchTracks(expression: String): ResultSearch
}