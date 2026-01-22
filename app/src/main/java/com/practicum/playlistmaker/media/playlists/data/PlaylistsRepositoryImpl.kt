package com.practicum.playlistmaker.media.playlists.data

import com.practicum.playlistmaker.data.db.DbConverter
import com.practicum.playlistmaker.data.db.TrackInPlaylistsDbConverter
import com.practicum.playlistmaker.data.db.dao.PlaylistsDao
import com.practicum.playlistmaker.data.db.dao.TrackInPlaylistsDao
import com.practicum.playlistmaker.data.db.entity.PlaylistsEntity
import com.practicum.playlistmaker.data.db.entity.TrackInPlaylistsEntity
import com.practicum.playlistmaker.media.playlists.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val playlistsDao: PlaylistsDao,
    private val trackInPlaylistDao: TrackInPlaylistsDao,
    private val playlistsDbConverter: DbConverter,
    private val trackInPlaylistsConverter: TrackInPlaylistsDbConverter

): PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsDao.insertPlaylists(playlistsDbConverter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsDao.deletePlaylist(playlistsDbConverter.map(playlist))
    }

    override suspend fun deleteAllPlaylists() {
        playlistsDao.clearPlaylists()
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistsDao.getPlaylists()
        emit(convertFromPlaylist(playlists))
    }

    private fun convertFromPlaylist(playlistsList:List<PlaylistsEntity>):List<Playlist>{
        return playlistsList.map { value ->  playlistsDbConverter.map(value)}
    }

    override suspend fun addTrackToPlaylist(
        playlist: Playlist,
        track: Track
    ) {
        playlistsDao.updatePlaylists(
            playlist.id,
            playlistsDbConverter.map(playlist.listIds + listOf(track.id)),
            playlist.listIds.size + 1
        )
    }

    override suspend fun addTrack(track: Track) {
        trackInPlaylistDao.insertTrack(trackInPlaylistsConverter.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        trackInPlaylistDao.deleteTrack(trackInPlaylistsConverter.map(track))
    }

    override fun getTrackById(id: Int): Flow<Track?> = flow {
        val track = trackInPlaylistDao.getTrackById(id)
        emit(convertromEntityToTrackF(track))
    }

    private fun convertromEntityToTrackF(trackEntity: TrackInPlaylistsEntity): Track {
        return trackInPlaylistsConverter.map(trackEntity)
    }
}