package com.practicum.playlistmaker.searchResult

import com.google.gson.annotations.SerializedName

class TracksResponse (
//    @SerializedName("results") val results : ArrayList<Track>,
//    @SerializedName("resultCount") val resultCount : Int
    val results : ArrayList<Track>,
    val resultCount : Int
)