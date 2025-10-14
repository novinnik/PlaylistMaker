package com.practicum.playlistmaker.setting.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.setting.domain.api.ThemeRepository

private const val DARK_THEME = "DARK_THEME"

class ThemeRepositoryImpl(private val sharedPreferences: SharedPreferences) : ThemeRepository{
    override fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }

    override fun saveTheme(darkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME, darkTheme).apply()
    }

    override fun switchTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}