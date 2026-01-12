package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.FavoritesDao
import com.practicum.playlistmaker.data.db.entity.FavoritesEntity

@Database(version = 1, entities = [FavoritesEntity::class], exportSchema = false)
abstract class FavoritesDataBase: RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}