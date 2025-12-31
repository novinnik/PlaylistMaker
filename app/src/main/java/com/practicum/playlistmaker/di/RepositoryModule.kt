package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.db.FavoritesDataBase
import com.practicum.playlistmaker.data.db.FavoritesDbConverter
import com.practicum.playlistmaker.media.favorites.data.FavoritesRepositoryImpl
import com.practicum.playlistmaker.media.favorites.domain.db.FavoritesRepository
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

    factory <TracksSearchRepository> {
        TracksSearchRepositoryImpl(get())
    }

    factory <HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    factory <ShareRepository> {
        ShareRepositoryImpl(androidContext())
    }

    factory <ThemeRepository> {
        ThemeRepositoryImpl(get())
    }

    factory { FavoritesDbConverter() }

    single {
        get<FavoritesDataBase>().favoritesDao()
    }

    factory <FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }
}