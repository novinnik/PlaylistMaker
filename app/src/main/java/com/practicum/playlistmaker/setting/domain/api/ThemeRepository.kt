package com.practicum.playlistmaker.setting.domain.api


interface ThemeRepository {
    fun isDarkTheme(): Boolean
    fun saveTheme(darkTheme: Boolean)
    fun switchTheme(darkTheme: Boolean)
}