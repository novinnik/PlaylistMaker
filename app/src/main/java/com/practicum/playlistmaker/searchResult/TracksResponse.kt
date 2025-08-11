package com.practicum.playlistmaker.searchResult

import com.google.gson.annotations.SerializedName

class TracksResponse (
    @SerializedName("results") val results : MutableList<Track>,
    @SerializedName("resultCount") val resultCount : Int
)