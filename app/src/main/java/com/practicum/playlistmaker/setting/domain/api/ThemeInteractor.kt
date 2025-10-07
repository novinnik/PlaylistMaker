package com.practicum.playlistmaker.setting.domain.api

interface ThemeInteractor {
    fun isDarkTheme(): Boolean
    fun saveTheme(darkTheme: Boolean)
    fun switchTheme(darkTheme: Boolean)
}