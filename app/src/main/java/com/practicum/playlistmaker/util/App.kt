package com.practicum.playlistmaker.util

import android.app.Application
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModule
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModule)
        }

        val themeInteractor: ThemeInteractor by inject()
        themeInteractor.switchTheme(themeInteractor.isDarkTheme())

    }

}