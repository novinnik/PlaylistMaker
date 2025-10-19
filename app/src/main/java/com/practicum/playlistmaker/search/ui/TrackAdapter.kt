package com.practicum.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.models.Track

class TrackAdapter(private val tracks: ArrayList<Track>, private val onTrackClick: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TrackViewHolder = TrackViewHolder.from(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onTrackClick(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}