package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.search.domain.models.ResultSearch
import com.practicum.playlistmaker.search.domain.models.Track

class TracksSearchRepositoryImpl(private val networkClient: NetworkClient): TracksSearchRepository {

    override fun searchTracks(expression: String): ResultSearch {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        val tracks = ArrayList<TrackDto>()
        var results: List<Track> = emptyList()

        if (response.resultCode == 200) {
            tracks.addAll((response as TracksSearchResponse).results)
            tracks.removeAll {
                it.trackName.isNullOrEmpty() || it.collectionName.isNullOrEmpty() ||
                        it.previewUrl.isNullOrEmpty() || it.trackTime == 0L
            }
            results =  tracks.map {
                Track(it.id, it.trackName, it.artistName, it.trackTime,
                    it.albumPoster, it.collectionName, it.releaseDate,
                    it.primaryGenreName, it.country, it.previewUrl)}
        }

        return ResultSearch(results, response.resultCode)
    }
}
