package com.practicum.playlistmaker.media.playlists.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media.playlists.ui.TrackInPlaylistAdapter
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistInfoViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Converter.getNoun
import com.practicum.playlistmaker.util.Converter.timeConversion
import com.practicum.playlistmaker.util.Converter.timeConversionMM
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInfoFragment : Fragment() {

    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!
    private var playlistId: Int = -1
    private val viewModel by viewModel<PlaylistInfoViewModel> { parametersOf(playlistId) }

    private lateinit var btmSheetTracks: BottomSheetBehavior<LinearLayout>
    private lateinit var btmSheetMenu: BottomSheetBehavior<LinearLayout>
    private lateinit var onClickDebounceTrack: (Track) -> Unit
    private lateinit var onClickLongDebounceTrack: (Track) -> Unit
    private lateinit var trackAdapter: TrackInPlaylistAdapter
    private var tracksList = arrayListOf<Track>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistId = requireArguments().getInt(PLAYLIST_ID)
        viewModel.updateState()

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })

        onClickDebounceTrack = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            startActivityPlayer(track)
        }

        onClickLongDebounceTrack = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            deleteTrack(track)
        }

        trackAdapter = TrackInPlaylistAdapter(
            tracksList,
            { track -> onClickDebounceTrack(track) },
            { track -> onClickLongDebounceTrack(track) }
        )

        binding.btmRecyclerView.adapter = trackAdapter

        viewModel.observeState().observe(viewLifecycleOwner) { it ->
            renderPlaylist(it.playlist)
            renderTracks(it.tracks)
        }

        initPeekHeightBottomSheet()

        btmSheetTracks = BottomSheetBehavior.from(binding.btmSheetTracks).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        btmSheetTracks.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> binding.overlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                val top = bottomSheet.top
                val screenHeight = resources.displayMetrics.heightPixels
                val alpha = 1f - (top.toFloat() / screenHeight.toFloat())
                binding.overlay.alpha = alpha.coerceIn(0f, 1f)
                binding.overlay.isVisible = alpha > 0f
            }

        })

        btmSheetMenu = BottomSheetBehavior.from(binding.btmSheetBtnMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        btmSheetMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> binding.overlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0) {
                    binding.overlay.alpha = slideOffset
                }
            }

        })

        binding.btnMenu.setOnClickListener {
            btmSheetMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        //клик по основному элементу экрана
        binding.root.setOnClickListener {
            if (btmSheetMenu.state == BottomSheetBehavior.STATE_COLLAPSED) {
                btmSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        binding.btnShare.setOnClickListener {
            showMessageShare()
        }

        binding.sharePlaylist.setOnClickListener {
            showMessageShare()
        }

        binding.deletePlaylist.setOnClickListener {
            deletePlaylist()
        }

        binding.editInfoPlaylist.setOnClickListener {
            startActivityEditPlaylist()
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.updateState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun renderPlaylist(playlist: Playlist) {
        loadImage(playlist.image)

        binding.apply {
            playlistName.text = playlist.title
            playlistDescription.text = playlist.description
            allCountTracks.text = countString(playlist.count)
            titlePlaylist.text = playlist.title
            countPlaylist.text = countString(playlist.count)
        }

    }

    private fun renderTracks(tracks: List<Track>) {
        binding.apply {
            allTimeTracks.text = getStringAllTimes(tracks)
            tvNoTracks.isVisible = !tracks.isNotEmpty()
            btmRecyclerView.isVisible = tracks.isNotEmpty()
        }
        tracksList.clear()
        tracksList.addAll(tracks)
        trackAdapter.updateData(tracks)
    }

    private fun loadImage(uri: Uri?) {
        if (uri != null) {
            Glide.with(requireContext())
                .load(uri)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(binding.playlistImage)

            Glide.with(requireContext())
                .load(uri)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(binding.imagePlaylist)
        }
    }

    private fun getStringAllTimes(tracks: List<Track>): String {
        var allTime = 0L
        for (track in tracks) {
            allTime += track.trackTime ?: 0L
        }
        val allTimeText = timeConversionMM(allTime)

        val countStr = getNoun(
            allTimeText.toInt(),
            requireContext().resources.getString(R.string.one_minute),
            requireContext().resources.getString(R.string.two_minutes),
            requireContext().resources.getString(R.string.zero_many_minutes)
        )

        return "$allTimeText $countStr"
    }

    private fun countString(count: Int): String {
        val countStr = getNoun(
            count,
            requireContext().resources.getString(R.string.one_track),
            requireContext().resources.getString(R.string.two_four_track),
            requireContext().resources.getString(R.string.zero_many_track)
        )

        return "$count $countStr"
    }

    private fun startActivityPlayer(trackClicked: Track) {
        findNavController().navigate(
            R.id.action_playlistInfoFragment_to_playerFragment,
            PlayerFragment.createArgs(trackClicked)
        )
    }

    private fun startActivityEditPlaylist() {
        findNavController().navigate(
            R.id.action_playlistInfoFragment_to_playlistAddFragment,
            PlaylistAddFragment.createArgs(playlistId)
        )
    }

    private fun deleteTrack(track: Track) {
        val questionMessage = requireContext().resources.getString(R.string.question_delete_track)
        MaterialAlertDialogBuilder(requireContext(), R.style.dialogTheme)
            .setMessage("$questionMessage?")
            .setNegativeButton(requireContext().resources.getString(R.string.string_no)) { dialog, which -> }
            .setPositiveButton(requireContext().resources.getString(R.string.string_yes)) { dialog, which ->
                viewModel.deleteTrackFromPlaylist(track.id)
            }.show()
    }

    private fun deletePlaylist() {

        binding.overlay.visibility = View.VISIBLE
        val questionMessage =
            requireContext().resources.getString(R.string.question_delete_playlist)
        MaterialAlertDialogBuilder(requireContext(), R.style.dialogTheme)
            .setTitle(requireContext().resources.getString(R.string.delete_playlist))
            .setMessage("$questionMessage \"${binding.playlistName.text}\"?")
            .setNegativeButton(requireContext().resources.getString(R.string.string_no)) { dialog, which ->
                binding.overlay.visibility = View.GONE
            }
            .setPositiveButton(requireContext().resources.getString(R.string.string_yes)) { dialog, which ->
                viewModel.deletePlaylist()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }.show()
    }

    private fun getMessageShare(): String {
        var message = binding.playlistName.text.toString() + "\n" +
                binding.playlistDescription.text.toString() + "\n" +
                tracksList.count() + " " +
                getNoun(
                    tracksList.count(),
                    requireContext().resources.getString(R.string.one_track),
                    requireContext().resources.getString(R.string.two_four_track),
                    requireContext().resources.getString(R.string.zero_many_track)
                ) + "\n"
        tracksList.forEachIndexed { index, track ->
            val number = index + 1

            message += "${number}. ${track.artistName} - ${track.trackName} (${timeConversion(track.trackTime)})"

            if (index < tracksList.size - 1) message += "\n"
        }
        return message
    }

    private fun showMessageShare() {
        if (!tracksList.isEmpty()) {
            viewModel.sharePlaylist(getMessageShare())
        } else {
            showMessageToast(requireContext().resources.getString(R.string.no_tracks_in_playlist_share))
        }
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun initPeekHeightBottomSheet() {
        val btmSheetBehavior = binding.btmSheetTracks
        val content = binding.buttonGroup
        btmSheetBehavior.post {
            val behavior = BottomSheetBehavior.from(btmSheetBehavior)
            val rootHeight = binding.root.height
            val contentBottom = content.bottom
            val peekHeight = rootHeight - contentBottom
            behavior.peekHeight = peekHeight
        }
    }

    companion object {
        private const val PLAYLIST_ID = "playlist_id"
        const val CLICK_DEBOUNCE_DELAY = 500L
        fun createArgs(playlistId: Int): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}