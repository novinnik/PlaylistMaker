package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.FavoritesDao
import com.practicum.playlistmaker.data.db.dao.TrackInPlaylistsDao
import com.practicum.playlistmaker.data.db.dao.PlaylistsDao
import com.practicum.playlistmaker.data.db.entity.FavoritesEntity
import com.practicum.playlistmaker.data.db.entity.PlaylistsEntity
import com.practicum.playlistmaker.data.db.entity.TrackInPlaylistsEntity

@Database(version = 1, entities = [FavoritesEntity::class, PlaylistsEntity::class, TrackInPlaylistsEntity::class])
abstract class AppDataBase: RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun playlistsDao(): PlaylistsDao
    abstract fun trackInPlaylistsDao(): TrackInPlaylistsDao
}