package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryInteractor {
    fun clearHistory(tracks: ArrayList<Track>)
    fun updateHistory(tracks: ArrayList<Track>): ArrayList<Track>
    fun addToHistory(newTrack : Track)
    fun getHistory() : ArrayList<Track>
}