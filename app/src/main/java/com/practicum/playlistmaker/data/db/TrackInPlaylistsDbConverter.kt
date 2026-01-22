package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.data.db.entity.TrackInPlaylistsEntity
import com.practicum.playlistmaker.search.domain.models.Track
import java.time.LocalDateTime

class TrackInPlaylistsDbConverter {

    fun map(track: Track): TrackInPlaylistsEntity {
        return TrackInPlaylistsEntity(
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

    fun map(track: TrackInPlaylistsEntity): Track {
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