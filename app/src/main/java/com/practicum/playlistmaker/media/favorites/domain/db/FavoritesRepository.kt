package com.practicum.playlistmaker.media.favorites.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getFavorites(): Flow<List<Track>>
    fun isFavoritesById(trackId: Int): Flow<Boolean>

}