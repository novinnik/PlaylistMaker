package com.practicum.playlistmaker.media.playlists.domain.db

import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun deleteAllPlaylists()
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    fun getPlaylistById(id: Int): Flow<Playlist>

    //Треки
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getTrackById(id:Int): Flow<Track?>

    suspend fun getTracksInPlaylist(playlist: Playlist):List<Track>
    suspend fun deleteTrackFromPlaylist(idTrack: Int, idPlaylist:Int)
    suspend fun deletePlaylistById(id: Int)
    suspend fun updatePlaylist(playlist: Playlist)
}