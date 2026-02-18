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
import kotlinx.coroutines.runBlocking

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
        playlistsDao.updateTracksInPlaylist(
            playlist.id,
            playlistsDbConverter.map(listOf(track.id) + playlist.listIds),
            playlist.listIds.size + 1
        )
    }

    override fun getPlaylistById(id: Int): Flow<Playlist> = flow {
        val playlistEntity = playlistsDao.getPlaylistById(id)
        emit(playlistsDbConverter.map(playlistEntity))
    }
    override suspend fun addTrack(track: Track) {
        trackInPlaylistDao.insertTrack(trackInPlaylistsConverter.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        trackInPlaylistDao.deleteTrack(trackInPlaylistsConverter.map(track))
    }

    override fun getTrackById(id: Int): Flow<Track?> = flow {
        val track = trackInPlaylistDao.getTrackById(id)
        emit(convertFromEntityToTracks(track))
    }

    override suspend fun getTracksInPlaylist(playlist: Playlist): List<Track> {
        val listTracks = mutableListOf<Track>()

        for (id in playlist.listIds){
            val track = convertFromEntityToTracks(trackInPlaylistDao.getTrackById(id))
            listTracks.add(track)
        }
        return listTracks
    }

    private fun convertFromEntityToTracks(trackEntity: TrackInPlaylistsEntity): Track {
        return trackInPlaylistsConverter.map(trackEntity)
    }

    override suspend fun deleteTrackFromPlaylist(idTrack: Int, idPlaylist: Int) {

        val playlist: Playlist = playlistsDbConverter.map(playlistsDao.getPlaylistById(idPlaylist))

        //перебрать спиское треков и удалить нужный, сминусовать количество
        val listIds: List<Int> = playlist.listIds - idTrack

        playlistsDao.updateTracksInPlaylist(
            idPlaylist,
            playlistsDbConverter.map(listIds),
            listIds.size
        )

        val isTrackUsed = trackInAllPlaylist(idTrack)

        //удалить трек если нет в плейлистах
        if (!isTrackUsed) {
            trackInPlaylistDao.deleteTrackById(idTrack)
        }
    }

    private suspend fun trackInAllPlaylist(idTrack: Int): Boolean{
        val allPlaylist = convertFromPlaylist(playlistsDao.getPlaylists())

        var isTrackUsed = false
        allPlaylist.map { playlist ->
            val trackIds = playlist.listIds
            if (trackIds.contains(idTrack)){
                isTrackUsed = true
            }
        }
        return isTrackUsed
    }

    override suspend fun deletePlaylistById(id: Int) {
        runBlocking {
            val playlist = playlistsDbConverter.map(playlistsDao.getPlaylistById(id))
            val listIds = playlist.listIds

            playlistsDao.deletePlaylistById(id)

            if (listIds.isNotEmpty()) cleanupTracks(listIds)
        }
        
    }

    private suspend fun cleanupTracks(idTracks: List<Int>) {
        idTracks.forEach {id ->
            val isTrackUsed = trackInAllPlaylist(id)

            if (!isTrackUsed) {
                trackInPlaylistDao.deleteTrackById(id)
            }
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val image = playlist.image?.toString() ?: ""
        playlistsDao.updateItemsInPlaylist(
            playlist.id,
            image,
            playlist.title,
            playlist.description
        )
    }
}