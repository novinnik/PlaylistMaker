package com.practicum.playlistmaker.player.ui.bottom_sheet

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist

class PlaylistBottomSheetAdapter(
    private val playlists: List<Playlist>,
    private val onPlayListClick: (Playlist) -> Unit
): RecyclerView.Adapter<PlaylistBottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBottomSheetViewHolder {
        return PlaylistBottomSheetViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onPlayListClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

}