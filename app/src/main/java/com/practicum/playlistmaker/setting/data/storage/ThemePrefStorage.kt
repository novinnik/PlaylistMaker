package com.practicum.playlistmaker.setting.data.storage

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.core.content.edit

class ThemePrefStorage(private val context: Context,
                       private val sharedPreferences: SharedPreferences) {

    private val DARK_THEME = "DARK_THEME"

    fun storeData(data: Boolean) {
        sharedPreferences.edit { putBoolean(DARK_THEME, data) }
    }

    fun getData(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, isDarkTheme(context))
    }

    private fun isDarkTheme(context: Context): Boolean {
        return when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

}