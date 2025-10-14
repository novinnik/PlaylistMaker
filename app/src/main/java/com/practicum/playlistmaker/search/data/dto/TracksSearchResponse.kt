package com.practicum.playlistmaker.search.data.dto

import com.practicum.playlistmaker.data.dto.NetworkResponse
import com.practicum.playlistmaker.data.dto.TrackDto

class TracksSearchResponse (
    val resultCount : Int,
    val results : List<TrackDto>
) : NetworkResponse()