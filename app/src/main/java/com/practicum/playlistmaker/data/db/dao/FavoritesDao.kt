package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.FavoritesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert(entity = FavoritesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(favoritesEntity: FavoritesEntity)

    @Delete(entity = FavoritesEntity::class)
    fun deleteTrack(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM favorites_table ORDER BY addTime DESC")
    fun getTracksFavorites(): Flow<List<FavoritesEntity>>

    @Query("SELECT trackId FROM favorites_table")
    fun getTracksFavoritesIds(): Flow<List<Int>>

//    @Query("SELECT EXISTS(SELECT 1 FROM favorites_table WHERE trackId = :id)")
//    fun getIdFavorites(id: Int): Flow<Boolean>
}