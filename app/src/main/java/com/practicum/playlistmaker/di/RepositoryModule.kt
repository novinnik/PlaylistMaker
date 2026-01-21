package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.db.AppDataBase
import com.practicum.playlistmaker.data.db.DbConverter
import com.practicum.playlistmaker.data.db.TrackInPlaylistsDbConverter
import com.practicum.playlistmaker.media.favorites.data.FavoritesRepositoryImpl
import com.practicum.playlistmaker.media.favorites.domain.db.FavoritesRepository
import com.practicum.playlistmaker.media.playlists.data.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.media.playlists.data.WorkingWithFilesRepositoryImpl
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.media.playlists.domain.db.WorkingWithFilesRepository
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

    factory { DbConverter(get()) }

    factory { TrackInPlaylistsDbConverter() }

    single {
        get<AppDataBase>().favoritesDao()
    }

    single{
        get<AppDataBase>().playlistsDao()
    }

    single{
        get<AppDataBase>().trackInPlaylistsDao()
    }

    factory <FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    factory <PlaylistsRepository>{
        PlaylistsRepositoryImpl(get(), get(), get(), get())
    }

    factory <WorkingWithFilesRepository>{
        WorkingWithFilesRepositoryImpl(androidContext())
    }
}