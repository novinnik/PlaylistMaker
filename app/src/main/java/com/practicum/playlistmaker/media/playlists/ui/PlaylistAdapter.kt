package com.practicum.playlistmaker.media.playlists.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist

class PlaylistAdapter(private val playlists: List<Playlist>, private val onTrackClick: (Playlist) -> Unit): RecyclerView.Adapter<PlaylistViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onTrackClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}