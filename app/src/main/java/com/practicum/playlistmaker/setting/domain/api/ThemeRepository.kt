package com.practicum.playlistmaker.setting.domain.api

import com.practicum.playlistmaker.Resource

interface ThemeRepository {
    fun isDarkTheme(): Resource<Boolean>
    fun switchTheme(darkTheme: Boolean)
}