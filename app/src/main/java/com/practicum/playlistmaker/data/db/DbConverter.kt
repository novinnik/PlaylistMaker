package com.practicum.playlistmaker.data.db

import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.entity.FavoritesEntity
import com.practicum.playlistmaker.data.db.entity.PlaylistsEntity
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import java.time.LocalDateTime

class DbConverter(private val gson: Gson) {

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
    fun map(playlist: Playlist): PlaylistsEntity{
        return PlaylistsEntity(
            0,
            image = playlist.image?.toString()?:"",
            title = playlist.title,
            description = playlist.description,
            listIds = gson.toJson(playlist.listIds),
            count = playlist.listIds.size,
            LocalDateTime.now().toString()
        )
    }

    fun map(playlist: PlaylistsEntity): Playlist{
        return Playlist(
            id = playlist.id,
            image = playlist.image.toUri(),
            title = playlist.title,
            description = playlist.description,
            listIds = gson.fromJson(playlist.listIds, object : TypeToken<List<Int>>() {}.type),
            count = playlist.count
        )
    }

    fun map(listIds:List<Int>): String{
        return gson.toJson(listIds)
    }
}