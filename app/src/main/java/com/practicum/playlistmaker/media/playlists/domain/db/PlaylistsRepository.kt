package com.practicum.playlistmaker.media.playlists.domain.db

import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun deleteAllPlaylists()
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

    //Треки
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getTrackById(id:Int): Flow<Track?>
}