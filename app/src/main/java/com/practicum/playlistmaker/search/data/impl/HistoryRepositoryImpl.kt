package com.practicum.playlistmaker.search.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import androidx.core.content.edit

private const val MAX_LIMIT_SIZE_HISTORY = 10

class HistoryRepositoryImpl(private val preferences: SharedPreferences, private val gson: Gson) : HistoryRepository{

    override fun clearHistory(tracks: ArrayList<Track>) {
        //добавить очистку в настройках
        tracks.clear()
        preferences.edit {
            remove(KEY_HISTORY_TRACKS)
        }
    }

    override fun updateHistory(tracks: ArrayList<Track>): ArrayList<Track>{
        tracks.clear()
        tracks.addAll(getHistory())
        return tracks;
    }

    override fun addToHistory(newTrack: Track) {
        val historyList = getHistory().toMutableList()
        //удаление элемента, если присутствует в списке
        historyList.removeAll{it.id == newTrack.id}
        //добавляем в начало
        historyList.add(0, newTrack)
        //проверяем на ограничение кол-ва
        if (historyList.size > MAX_LIMIT_SIZE_HISTORY){
            historyList.removeAt(historyList.size - 1)
        }
        //сохраним новый список
        saveHistory(historyList)
    }

    override fun getHistory(): ArrayList<Track> {
        val historyJson = preferences.getString(KEY_HISTORY_TRACKS, null)
        return if (!historyJson.isNullOrEmpty()) {
            gson.fromJson(historyJson, object : TypeToken<List<Track>>() {}.type)
        } else {
            arrayListOf()
        }
    }

    fun saveHistory(tracks : List<Track>){
        val historyJson = gson.toJson(tracks)
        preferences.edit {
            putString(KEY_HISTORY_TRACKS, historyJson)
        }
    }

    companion object{
        private const val KEY_HISTORY_TRACKS = "key_history_tracks"
    }

}

