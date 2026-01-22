package com.practicum.playlistmaker.media.playlists.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistViewBinding
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.util.Converter.dpToPx
import com.practicum.playlistmaker.util.Converter.getNoun

class PlaylistViewHolder(
    private val binding: PlaylistViewBinding,
    private val context: Context
): RecyclerView.ViewHolder(binding.root){

    fun bind(playlist: Playlist){
        val cornerRadius = dpToPx(CORNER_RADIUS, itemView.context)

        Glide.with(binding.root)
            .load(playlist.image)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.imagePlaylist)

        binding.titlePlaylist.text = playlist.title
        binding.countPlaylist.text = countString(playlist.count)
    }

    private fun countString(count: Int): String{
        val countStr = getNoun(
            count,
            context.resources.getString(R.string.one_track),
            context.resources.getString(R.string.two_four_track),
            context.resources.getString(R.string.zero_many_track))

        return "$count $countStr"
    }
    companion object{
        private const val CORNER_RADIUS = 8f

        fun from(parent: ViewGroup): PlaylistViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistViewBinding.inflate(inflater, parent, false)
            return PlaylistViewHolder(binding, parent.context)
        }
    }

}