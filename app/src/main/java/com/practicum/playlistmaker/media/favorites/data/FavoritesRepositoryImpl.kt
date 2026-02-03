package com.practicum.playlistmaker.media.favorites.data
import com.practicum.playlistmaker.data.db.DbConverter
import com.practicum.playlistmaker.data.db.dao.FavoritesDao
import com.practicum.playlistmaker.data.db.entity.FavoritesEntity
import com.practicum.playlistmaker.media.favorites.domain.db.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.collections.map

class FavoritesRepositoryImpl(
    private val favoritesDao: FavoritesDao,
    private val dbConverter: DbConverter
) : FavoritesRepository {

    override suspend fun addTrack(track: Track) {
        favoritesDao.insertTrack(dbConverter.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        favoritesDao.deleteTrack(dbConverter.map((track)))
    }

    override fun getFavorites(): Flow<List<Track>> = flow {
        val favoritesList = favoritesDao.getTracksFavorites()
       emit(convertFromTrack(favoritesList))
    }

    private fun convertFromTrack(favorites: List<FavoritesEntity>):List<Track>{
        return favorites.map { favorite -> dbConverter.map(favorite)}
    }

    override fun isFavoritesById(trackId: Int): Flow<Boolean> = flow {
        emit( favoritesDao.getIdFavorites(trackId))
    }

}