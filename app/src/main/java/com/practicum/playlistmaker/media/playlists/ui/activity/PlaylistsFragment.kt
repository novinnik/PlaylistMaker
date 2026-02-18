package com.practicum.playlistmaker.media.playlists.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media.playlists.model.PlaylistState
import com.practicum.playlistmaker.media.playlists.ui.PlaylistAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.android.ext.android.inject

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by inject()
    private var playlist = arrayListOf<Playlist>()
    private lateinit var onClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }

        binding.mediaButtonNewPlaylist.setOnClickListener {
            addNewPlayList()
        }

        onClickDebounce = debounce<Playlist>(CLICK_DEBOUNCE_DELAY,viewLifecycleOwner.lifecycleScope, false){playlist ->
            startActivityInfo(playlist)
        }

        binding.playlistsRecycler.adapter = PlaylistAdapter(playlist){value->
            onClickDebounce(value)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.playlistsRecycler.adapter = null
        _binding = null
    }

    private fun addNewPlayList(){
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistAddFragment,
            PlaylistAddFragment.createArgs(-1)
        )
    }

    fun render(state: PlaylistState){
        when(state){
            is PlaylistState.Loading -> showLoading()
            is PlaylistState.Content -> showContent(state.playlist)
            is PlaylistState.Empty -> showEmpty()
        }
    }

    fun showLoading() {
        binding.apply {
            playlistsProgressBar.visibility = View.VISIBLE
            errorImageFragmentPlaylists.visibility = View.GONE
            errorMessageFragmentPlaylists.visibility = View.GONE
            playlistsRecycler.visibility = View.GONE
        }
    }

    fun showContent(playlisContentt: List<Playlist>) {
        binding.apply {
            playlistsProgressBar.visibility = View.GONE
            errorImageFragmentPlaylists.visibility = View.GONE
            errorMessageFragmentPlaylists.visibility = View.GONE
            playlistsRecycler.visibility = View.VISIBLE
        }
        playlist.clear()
        playlist.addAll(playlisContentt)
        binding.playlistsRecycler.adapter?.notifyDataSetChanged()
    }

    fun showEmpty() {
        binding.apply {
            playlistsProgressBar.visibility = View.GONE
            errorImageFragmentPlaylists.visibility = View.VISIBLE
            errorMessageFragmentPlaylists.visibility = View.VISIBLE
            playlistsRecycler.visibility = View.GONE
        }
        playlist.clear()
        binding.playlistsRecycler.adapter?.notifyDataSetChanged()
    }

    private fun startActivityInfo(playlistClicked: Playlist) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistInfoFragment,
            PlaylistInfoFragment.createArgs(playlistClicked.id)
        )
    }
    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = PlaylistsFragment()
    }
}