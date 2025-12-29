package com.practicum.playlistmaker.media.favorites.data

import com.practicum.playlistmaker.data.db.FavoritesDataBase
import com.practicum.playlistmaker.data.db.FavoritesDbConverter
import com.practicum.playlistmaker.media.favorites.domain.db.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.collections.map

class FavoritesRepositoryImpl(
    private val favoritesDataBase: FavoritesDataBase,
    private val favoritesDbConverter: FavoritesDbConverter
) : FavoritesRepository {

    override suspend fun addTrack(track: Track) {
        withContext(Dispatchers.IO){
            favoritesDataBase.favoritesDao().insertTrack(favoritesDbConverter.map(track))
        }

    }

    override suspend fun deleteTrack(track: Track) {
        withContext(Dispatchers.IO) {
            favoritesDataBase.favoritesDao().deleteTrack(favoritesDbConverter.map((track)))
        }
    }

    override fun getFavorites(): Flow<List<Track>> {
        return favoritesDataBase
            .favoritesDao()
            .getTracksFavorites()
            .map { favoritesEntities ->
                favoritesEntities
                    .map { entity ->
                        favoritesDbConverter.map(entity)
                    }
            }
    }

}