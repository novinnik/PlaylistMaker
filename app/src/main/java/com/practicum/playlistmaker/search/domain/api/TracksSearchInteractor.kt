package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.ResultSearch
import kotlinx.coroutines.flow.Flow

interface TracksSearchInteractor {

    fun searchTracks(expression: String): Flow<ResultSearch>
}