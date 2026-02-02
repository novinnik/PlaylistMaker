package com.practicum.playlistmaker.player.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.main.ui.MainActivity
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media.playlists.model.PlaylistState
import com.practicum.playlistmaker.media.playlists.ui.activity.PlaylistAddFragment
import com.practicum.playlistmaker.player.ui.bottom_sheet.PlaylistBottomSheetAdapter
import com.practicum.playlistmaker.player.ui.models.PlayerStatus
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Converter.dpToPx
import com.practicum.playlistmaker.util.Converter.timeConversion
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlayerFragment: Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private var trackUrl : String? = null
    private val viewModel by viewModel<PlayerViewModel>{ parametersOf(trackUrl) }
    private var timeProgress = 0L
    private var currentTrack: Track? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var onClickDebouncePlaylist: (Playlist) -> Unit
    private val playlist = mutableListOf<Playlist>()
    private lateinit var playlistAdapter: PlaylistBottomSheetAdapter


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

        currentTrack?.let {
            addMediaTrack(it)
            viewModel.isFavoriteTrack(it)
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            changeImageButtonPlay(it)
            binding.playTrackProgress.text = it.timeProgress
        }

        viewModel.observeIsFavorite().observe(viewLifecycleOwner){
            changeImageButtonFavorite(it)
        }

        viewModel.observePlaylistsState().observe(viewLifecycleOwner){
            render(it)
        }

        binding.btnPlay.setOnClickListener{
            viewModel.playerControl()
        }

        binding.btnFavorite.setOnClickListener {
            currentTrack?.let {viewModel.onFavoriteClicked(it)}
        }

        showBottomSheet()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.playlistBtmRecyclerView.adapter = null
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

    private fun showBottomSheet(){

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBtmSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    //полностью свернуто, скрываем
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.playlistOverlay.visibility = View.GONE
                    }
                    else -> {
                        binding.playlistOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        binding.btnQueue.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            viewModel.getPlaylists()
        }

        binding.playlistBtmAdd.setOnClickListener {
            findNavController().navigate(
                R.id.action_playerFragment_to_playlistAddFragment,
                PlaylistAddFragment.createArgs(-1))
        }

        binding.playlistBtmRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        onClickDebouncePlaylist = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false)
            { value ->
                addTrackToPlaylist(value)
            }
        playlistAdapter = PlaylistBottomSheetAdapter(playlist) {newPlaylist ->
            onClickDebouncePlaylist(newPlaylist)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN}
        binding.playlistBtmRecyclerView.adapter = playlistAdapter



    }

    private fun addTrackToPlaylist(playlist: Playlist){
        currentTrack?.let {
            val isTrackInPlaylist = viewModel.trackInPlaylist(playlist, currentTrack!!)

            binding.playlistBtmRecyclerView.let {
                if (!isTrackInPlaylist) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    viewModel.addTrackToPlaylist(playlist, currentTrack!!)
                    showMessageToast("Добавлено в плейлист ${playlist.title}")
                } else {
                    showMessageToast("Трек уже добавлен в плейлист ${playlist.title}")
                }
            }
        }
    }

    fun render(state: PlaylistState){
        when(state){
            is PlaylistState.Loading, is PlaylistState.Empty -> showEmpty()
            is PlaylistState.Content -> showContent(state.playlist)
        }
    }

    fun showContent(newPlaylists: List<Playlist>) {

        binding.playlistBtmRecyclerView.visibility = View.VISIBLE

        this.playlist.clear()
        this.playlist.addAll(newPlaylists)
        playlistAdapter.notifyDataSetChanged()

//        binding.playlistBtmRecyclerView.adapter =
//            PlaylistBottomSheetAdapter(newPlaylists) {newPlaylist -> onClickDebouncePlaylist(newPlaylist)}

    }

    fun showEmpty() {
        binding.playlistBtmRecyclerView.visibility = View.GONE
    }


    private fun showMessageToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object{
        private const val CORNER_RADIUS = 8f
        private const val MEDIA_TRACK_KEY = "media_track_key"
        const val CLICK_DEBOUNCE_DELAY = 300L

        fun createArgs(track: Track): Bundle =
            bundleOf(MEDIA_TRACK_KEY to track)
    }
}