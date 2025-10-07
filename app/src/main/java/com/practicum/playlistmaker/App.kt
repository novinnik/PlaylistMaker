package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "PLAYLIST_MAKER_PREFERENCES"
const val BASE_URL_ITUNES = "https://itunes.apple.com"
const val DARK_THEME = "DARK_THEME"
//const val TRACK_HISTORY = "TRACK_HISTORY"
const val MAX_LIMIT_SIZE_HISTORY = 10
const val CORNER_RADIUS = 8f
const val MEDIA_TRACK_KEY = "media_track_key"
const val PLAY_DELAY = 500L
const val KEY_HISTORY_TRACKS = "key_history_tracks"
const val SEARCH_HISTORY_PREF = "search_history_pref"

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        applyDarkTheme(getDarkTheme())
    }

    private fun getDarkTheme(): Boolean{
        val darkTheme =
            when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false}
        val sharedPreferences = Creator.provideSharedPreferences()
        return sharedPreferences.getBoolean(DARK_THEME, darkTheme)
    }

    private fun applyDarkTheme(darkTheme: Boolean){
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }


}