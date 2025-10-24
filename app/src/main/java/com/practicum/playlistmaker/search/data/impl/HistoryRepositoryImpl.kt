package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.search.data.local.SearchHistory
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

private const val MAX_LIMIT_SIZE_HISTORY = 10

class HistoryRepositoryImpl() : HistoryRepository{

    val historySharedPreferences = Creator.provideSharedPreferences()
    val historySearch = SearchHistory(historySharedPreferences)

    override fun clearHistory(tracks: ArrayList<Track>) {
        //добавить очистку в настройках
        tracks.clear()
        historySearch.clearHistory()
    }

    override fun updateHistory(tracks: ArrayList<Track>): ArrayList<Track>{
        tracks.clear()
        tracks.addAll(historySearch.getHistory())
        return tracks;
    }

    override fun addToHistory(tracks: ArrayList<Track>, newTrack: Track) {
        historySearch.addToHistory(newTrack)
    }

    override fun getHistory(): ArrayList<Track> {
        return historySearch.getHistory() as ArrayList<Track>
    }

}