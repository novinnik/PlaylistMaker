package com.practicum.playlistmaker.search.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.models.Track

private const val MAX_LIMIT_SIZE_HISTORY = 10
private const val KEY_HISTORY_TRACKS = "key_history_tracks"

class SearchHistory(private val preferences: SharedPreferences) {
    private val gson = Gson()

    //получить список треков из истории
    fun getHistory(): List<Track> {
        val historyJson = preferences.getString(KEY_HISTORY_TRACKS, null)
        return if (!historyJson.isNullOrEmpty()) {
            gson.fromJson(historyJson, object : TypeToken<List<Track>>() {}.type)
        } else {
            emptyList()
        }
    }

    fun addToHistory(newTrack : Track){
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

    fun saveHistory(tracks : List<Track>){
        val historyJson = gson.toJson(tracks)
        preferences.edit()
            .putString(KEY_HISTORY_TRACKS, historyJson)
            .apply()
    }

    fun clearHistory(){
        preferences.edit()
            .remove(KEY_HISTORY_TRACKS)
            .apply()
    }

}