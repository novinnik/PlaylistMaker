package com.practicum.playlistmaker.media.playlists.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist

class PlaylistAdapter(private val playlist: List<Playlist>): RecyclerView.Adapter<PlaylistViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlist[position])
    }

    override fun getItemCount(): Int {
        return playlist.size
    }
}