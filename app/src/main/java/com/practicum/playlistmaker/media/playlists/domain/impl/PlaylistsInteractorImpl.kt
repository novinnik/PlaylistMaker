package com.practicum.playlistmaker.media.playlists.domain.impl

import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    val playlistsRepository: PlaylistsRepository,
): PlaylistsInteractor{
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsRepository.deletePlaylist(playlist)
    }

    override suspend fun deleteAllPlaylists() {
        playlistsRepository.deleteAllPlaylists()
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
       return playlistsRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(
        playlist: Playlist,
        track: Track
    ) {
        playlistsRepository.addTrackToPlaylist(playlist, track)
    }

    override fun getPlaylistById(id: Int): Flow<Playlist> {
        return playlistsRepository.getPlaylistById(id)
    }

    override suspend fun addTrack(track: Track) {
        playlistsRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        playlistsRepository.deleteTrack(track)
    }

    override fun getTrackById(id: Int): Flow<Track?> {
        return playlistsRepository.getTrackById(id)
    }

    override suspend fun getTracksInPlaylist(playlist: Playlist): List<Track> {
        return playlistsRepository.getTracksInPlaylist(playlist)
    }

    override suspend fun deleteTrackFromPlaylist(idTrack: Int, idPlaylist: Int) {
        playlistsRepository.deleteTrackFromPlaylist(idTrack, idPlaylist)
    }

    override suspend fun deletePlaylistById(id: Int) {
        playlistsRepository.deletePlaylistById(id)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistsRepository.updatePlaylist(playlist)
    }
}