package com.practicum.playlistmaker.util

import android.app.Application
import com.practicum.playlistmaker.util.Creator

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        val themeInteractor = Creator.provideThemeInteractor()
        themeInteractor.switchTheme(themeInteractor.isDarkTheme())

    }

}