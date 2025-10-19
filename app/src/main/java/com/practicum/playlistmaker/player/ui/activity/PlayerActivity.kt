package com.practicum.playlistmaker.player.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.models.PlayerStatus
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.dpToPx
import com.practicum.playlistmaker.search.domain.models.timeConversion

private const val CORNER_RADIUS = 8f
private const val MEDIA_TRACK_KEY = "media_track_key"

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var viewModel: PlayerViewModel? = null
    private var timeProgress = 0L
    private var trackUrl : String? = null
    private var currentTrack: Track? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarActivityPlayer.setNavigationOnClickListener { finish() }

        currentTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MEDIA_TRACK_KEY, Track::class.java)
        } else {
            @Suppress("DEPRECATION") // Для старых версий SDK
            intent.getParcelableExtra(MEDIA_TRACK_KEY)
        }

        currentTrack?.let { addMediaTrack(it) }

        viewModel = ViewModelProvider(this, PlayerViewModel.getFactory(trackUrl))
            .get(PlayerViewModel::class.java)

        viewModel?.observePlayerState()?.observe(this) {
            changeImageButtonPlay(it)
        }

        viewModel?.observePlayerTimer()?.observe(this) {
            binding.playTrackProgress.text = it
        }

        binding.btnPlay.setOnClickListener{
            viewModel?.playerControl()
        }
    }

    private fun addMediaTrack(track: Track) {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, this)))
            .into(binding.playImagePoster)

        binding.playTrackName.text = track.trackName
        binding.playArtistName.text = track.artistName

        binding.playTrackProgress.text = timeConversion(timeProgress)
        binding.playTrackDurationValue.text = timeConversion(track.trackTime)

        if (track.collectionName.isNullOrEmpty()) {
            binding.playGroupCollection.visibility = View.GONE
        } else {
            binding.playTrackCollectionValue.text = track.collectionName
            binding.playGroupCollection.visibility = View.VISIBLE
        }

        if (track.releaseDate.isNullOrEmpty()) {
            binding.playGroupYear.visibility = View.GONE
        } else {
            binding.playTrackYearValue.text = track.getYearDateRelease()
            binding.playGroupYear.visibility = View.VISIBLE
        }

        binding.playTrackGenreValue.text = track.primaryGenreName ?: ""
        binding.playTrackCountryValue.text = track.country ?: ""

        trackUrl = track.previewUrl ?:""
    }

    override fun onPause() {
        super.onPause()
        viewModel?.playerPause()
    }

    private fun changeImageButtonPlay(playerState: PlayerStatus){
        when(playerState){
            PlayerStatus.PLAY -> binding.btnPlay.setImageResource(R.drawable.ic_pause)
            PlayerStatus.PAUSE, PlayerStatus.PREPARED -> binding.btnPlay.setImageResource(R.drawable.ic_play)
            PlayerStatus.DEFAULT -> {binding.btnPlay.setImageResource(R.drawable.ic_play)}
        }
    }
}