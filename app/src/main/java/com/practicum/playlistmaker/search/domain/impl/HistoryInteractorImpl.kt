package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(private val historyRepository: HistoryRepository): HistoryInteractor {

    override fun clearHistory(tracks: ArrayList<Track>){
        return historyRepository.clearHistory(tracks)
    }

    override fun updateHistory(tracks: ArrayList<Track>): ArrayList<Track> {
        return historyRepository.updateHistory(tracks)
    }

    override fun addToHistory(newTrack: Track) {
        historyRepository.addToHistory(newTrack)
    }

    override fun getHistory(): ArrayList<Track> {
        return historyRepository.getHistory()
    }

}