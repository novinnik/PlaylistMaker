package com.practicum.playlistmaker.setting.domain.impl

import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeRepository

class ThemeInteractorImpl(private val themeRepository: ThemeRepository): ThemeInteractor{
    override fun isDarkTheme(): Boolean {
        return themeRepository.isDarkTheme()
    }

    override fun saveTheme(darkTheme: Boolean) {
        return themeRepository.saveTheme(darkTheme)
    }

    override fun switchTheme(darkTheme: Boolean) {
        return themeRepository.switchTheme(darkTheme)
    }

}