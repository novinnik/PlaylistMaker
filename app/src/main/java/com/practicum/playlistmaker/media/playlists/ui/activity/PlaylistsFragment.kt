package com.practicum.playlistmaker.media.playlists.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media.playlists.model.PlaylistState
import com.practicum.playlistmaker.media.playlists.ui.PlaylistAdapter
import org.koin.android.ext.android.inject

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by inject()

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

        viewModel.fillData()

        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }

        binding.mediaButtonNewPlaylist.setOnClickListener {
            addNewPlayList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addNewPlayList(){
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistAddFragment
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

    fun showContent(playlist: List<Playlist>) {
        binding.apply {
            playlistsProgressBar.visibility = View.GONE
            errorImageFragmentPlaylists.visibility = View.GONE
            errorMessageFragmentPlaylists.visibility = View.GONE
            playlistsRecycler.visibility = View.VISIBLE
            binding.playlistsRecycler.adapter = PlaylistAdapter(playlist)
        }
    }

    fun showEmpty() {
        binding.apply {
            playlistsProgressBar.visibility = View.GONE
            errorImageFragmentPlaylists.visibility = View.VISIBLE
            errorMessageFragmentPlaylists.visibility = View.VISIBLE
            playlistsRecycler.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}