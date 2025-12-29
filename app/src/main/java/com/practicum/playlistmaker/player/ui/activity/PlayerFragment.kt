package com.practicum.playlistmaker.player.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.ui.models.PlayerStatus
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Converter.dpToPx
import com.practicum.playlistmaker.util.Converter.timeConversion
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlayerFragment: Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private var trackUrl : String? = null
    private var isFavorite:Boolean = false
    private val viewModel by viewModel<PlayerViewModel>{ parametersOf(trackUrl, isFavorite) }
    private var timeProgress = 0L
    private var currentTrack: Track? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarActivityPlayer.setOnClickListener { findNavController().navigateUp() }

        currentTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(MEDIA_TRACK_KEY, Track::class.java)
        } else {
            @Suppress("DEPRECATION") // Для старых версий SDK
            requireArguments().getParcelable(MEDIA_TRACK_KEY)
        }
        currentTrack?.let { addMediaTrack(it) }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            changeImageButtonPlay(it)
            binding.playTrackProgress.text = it.timeProgress
        }

        viewModel.observeIsFavorite().observe(viewLifecycleOwner){
            changeImageButtonFavorite(currentTrack?.isFavorite?:false)
        }
        binding.btnPlay.setOnClickListener{
            viewModel.playerControl()
        }

        binding.btnFavorite.setOnClickListener {
            currentTrack?.let {viewModel.onFavoriteClicked(it)}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addMediaTrack(track: Track) {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, requireContext())))
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

        isFavorite = track.isFavorite
    }

    override fun onPause() {
        super.onPause()
        viewModel.playerPause()
    }

    private fun changeImageButtonPlay(playerState: PlayerStatus){
        when(playerState){
            is PlayerStatus.Play -> binding.btnPlay.setImageResource(R.drawable.ic_pause)
            is PlayerStatus.Pause, is PlayerStatus.Prepared -> binding.btnPlay.setImageResource(R.drawable.ic_play)
            is PlayerStatus.Default -> {binding.btnPlay.setImageResource(R.drawable.ic_play)}
        }
    }

    private fun changeImageButtonFavorite(isFavorite:Boolean){
        if (isFavorite){
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_like)
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
        }
    }

    companion object{
        private const val CORNER_RADIUS = 8f
        private const val MEDIA_TRACK_KEY = "media_track_key"

        fun createArgs(track: Track): Bundle =
            bundleOf(MEDIA_TRACK_KEY to track)
    }
}