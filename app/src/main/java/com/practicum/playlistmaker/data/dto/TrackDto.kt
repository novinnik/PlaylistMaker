package com.practicum.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackId") val id  : Int,
    val trackName : String?, //название трека
    val artistName : String?,   //имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime : Long?, //продолжительность трека
    @SerializedName("artworkUrl100") val albumPoster : String?, //обложка альбома
    val collectionName : String?, //название альбома
    val releaseDate: String?, //год релиза трека
    val primaryGenreName: String?, //жанр трека
    val country: String?, //страна исполнителя
    val previewUrl : String? //ссылка на отрывок трека
)