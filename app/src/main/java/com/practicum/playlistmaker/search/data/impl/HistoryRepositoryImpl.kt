package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.Creator
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
        tracks.clear()

        tracks.addAll(historySearch.getHistory())
        //удаление элемента, если присутствует в списке
        tracks.removeAll{it.id == newTrack.id}
        //добавляем в начало
        tracks.add(0, newTrack)
        //проверяем на ограничение кол-ва
        if (tracks.size > MAX_LIMIT_SIZE_HISTORY){
            tracks.removeAt(tracks.size - 1)
        }
        //сохраним новый список
        historySearch.saveHistory(tracks)
    }

}