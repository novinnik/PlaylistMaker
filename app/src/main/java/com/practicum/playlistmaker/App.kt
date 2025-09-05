package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App:Application() {
    var darkTheme = false
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        darkTheme = sharedPreferences.getBoolean(DARK_THEME, false)

        applyDarkTheme()

    }

    fun switchTheme(darkThemeEnabled : Boolean){
        darkTheme = darkThemeEnabled

        sharedPreferences.edit()
            .putBoolean(DARK_THEME, darkTheme)
            .apply()

        applyDarkTheme()

    }

    fun applyDarkTheme(){
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object{
        const val PLAYLIST_MAKER_PREFERENCES = "PLAYLIST_MAKER_PREFERENCES"
        const val DARK_THEME = "DARK_THEME"
    }
}