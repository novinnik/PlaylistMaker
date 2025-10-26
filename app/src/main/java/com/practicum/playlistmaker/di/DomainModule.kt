package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TracksSearchRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksSearchInteractor
import com.practicum.playlistmaker.search.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksSearchInteractorImpl
import com.practicum.playlistmaker.setting.data.impl.ShareRepositoryImpl
import com.practicum.playlistmaker.setting.data.impl.ThemeRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.ShareInteractor
import com.practicum.playlistmaker.setting.domain.api.ShareRepository
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeRepository
import com.practicum.playlistmaker.setting.domain.impl.ShareInteractorImpl
import com.practicum.playlistmaker.setting.domain.impl.ThemeInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {

    single <PlayerInteractor>{
        PlayerInteractorImpl(get())
    }

    single <TracksSearchInteractor> {
        TracksSearchInteractorImpl(get())
    }

    single <HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single <ShareInteractor> {
        ShareInteractorImpl(get())
    }

    single <ThemeInteractor> {
        ThemeInteractorImpl(get())
    }
}


val repositoryModule = module {

    single <PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    single <TracksSearchRepository> {
        TracksSearchRepositoryImpl(get())
    }

    single <HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    single <ShareRepository> {
        ShareRepositoryImpl(androidContext())
    }

    single <ThemeRepository> {
        ThemeRepositoryImpl(get())
    }
}