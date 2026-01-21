package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.TrackInPlaylistsEntity

@Dao
interface TrackInPlaylistsDao {

    @Insert(entity = TrackInPlaylistsEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(playlistTrackEntity: TrackInPlaylistsEntity)

    @Delete(entity = TrackInPlaylistsEntity::class)
    suspend fun deleteTrack(playlistTrackEntity: TrackInPlaylistsEntity)

    @Query("SELECT * FROM playlist_track_entity WHERE trackId = :id")
    suspend fun getTrackById(id:Int): TrackInPlaylistsEntity
}