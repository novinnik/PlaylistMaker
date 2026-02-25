package com.practicum.playlistmaker.media.playlists.model

import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistInfoState (
    val playlist: Playlist,
    val tracks: List<Track>
){}