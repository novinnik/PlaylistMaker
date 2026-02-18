package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.data.db.entity.PlaylistsEntity

@Dao
interface PlaylistsDao {
    @Insert(entity = PlaylistsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlistsEntity: PlaylistsEntity)

    @Delete(entity = PlaylistsEntity::class)
    suspend fun deletePlaylist(playlistsEntity: PlaylistsEntity)

    @Update(entity = PlaylistsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlistsEntity: PlaylistsEntity)

    @Query("DELETE FROM playlists_table")
    suspend fun clearPlaylists()

    @Query("SELECT * FROM playlists_table ORDER BY addTime DESC")
    suspend fun getPlaylists(): List<PlaylistsEntity>

    @Query("SELECT * FROM playlists_table WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId:Int): PlaylistsEntity

    @Query("UPDATE playlists_table SET listIds = :newListIds, count = :newCount WHERE id = :playlistId ")
    suspend fun updateTracksInPlaylist(playlistId:Int, newListIds: String, newCount: Int)

    @Query("DELETE FROM playlists_table WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId:Int)

    @Query("UPDATE playlists_table SET image = :newImage, title = :newTitle, description = :newDescription WHERE id = :playlistId ")
    suspend fun updateItemsInPlaylist(playlistId:Int, newImage: String, newTitle: String, newDescription: String)
}