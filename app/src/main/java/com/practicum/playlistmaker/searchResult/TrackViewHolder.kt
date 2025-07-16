package com.practicum.playlistmaker.searchResult

import android.content.Context
import android.util.TypedValue
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

    private val CORNER_RADIUS = 2f
    private val searchView : LinearLayout = itemView.findViewById(R.id.search_view)
    private val trackName : TextView = itemView.findViewById(R.id.track_name)
    private val artistName : TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime : TextView = itemView.findViewById(R.id.track_time)
    private val artwork : ImageView = itemView.findViewById(R.id.artwork_image)

    fun bind(item : Track){

        val cornerRadius = dpToPx(CORNER_RADIUS, itemView.context)

        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .into(artwork)

        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTime
    }

}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()
}