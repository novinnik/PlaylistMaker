package com.practicum.playlistmaker

import android.app.Application

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        val themeInteractor = Creator.provideThemeInteractor()
        themeInteractor.switchTheme(themeInteractor.isDarkTheme())

    }

}