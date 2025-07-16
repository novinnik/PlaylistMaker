package com.practicum.playlistmaker.searchResult

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R

class TrackViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    private val searchView : LinearLayout = itemView.findViewById(R.id.search_view)
    private val trackName : TextView = itemView.findViewById(R.id.track_name)
    private val artistName : TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime : TextView = itemView.findViewById(R.id.track_time)
    private val artwork : ImageView = itemView.findViewById(R.id.artwork_image)

    fun bind(item : Track){

        val imageArtWorkUrl = item.artworkUrl100

        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(2))
            .into(artwork)

        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTime
    }



}