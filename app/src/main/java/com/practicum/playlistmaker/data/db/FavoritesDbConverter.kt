package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.data.db.entity.FavoritesEntity
import com.practicum.playlistmaker.search.domain.models.Track
import java.time.LocalDateTime

class FavoritesDbConverter {
    fun map(track: Track): FavoritesEntity {
        return FavoritesEntity(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.albumPoster,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            LocalDateTime.now().toString()
        )
    }

    fun map(track: FavoritesEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.albumPoster,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            true
        )
    }
}