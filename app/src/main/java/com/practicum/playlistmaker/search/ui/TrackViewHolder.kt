package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.SearchTrackViewBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Converter.dpToPx
import com.practicum.playlistmaker.util.Converter.timeConversion

class TrackViewHolder(private val binding: SearchTrackViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item : Track){

        val cornerRadius = dpToPx(CORNER_RADIUS, itemView.context)

        Glide.with(binding.root)
            .load(item.albumPoster)
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.artworkImage)

        binding.trackName.text = item.trackName
        binding.artistName.text = item.artistName

        binding.trackTime.text = timeConversion(item.trackTime)
    }

    companion object{
        private const val CORNER_RADIUS = 2f
        fun from(parent: ViewGroup): TrackViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = SearchTrackViewBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }

}