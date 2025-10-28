package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TracksSearchRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.setting.data.impl.ShareRepositoryImpl
import com.practicum.playlistmaker.setting.data.impl.ThemeRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.ShareRepository
import com.practicum.playlistmaker.setting.domain.api.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory <PlayerRepository> {
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