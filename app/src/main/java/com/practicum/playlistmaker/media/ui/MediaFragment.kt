package com.practicum.playlistmaker.media.ui

import MediaScreen
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding
import com.practicum.playlistmaker.media.favorites.model.FavoriteState
import com.practicum.playlistmaker.media.favorites.ui.view_model.FavoritesViewModel
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media.playlists.model.PlaylistState
import com.practicum.playlistmaker.media.playlists.ui.activity.PlaylistAddFragment
import com.practicum.playlistmaker.media.playlists.ui.activity.PlaylistInfoFragment
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.theme.ThemeProject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MediaFragment: Fragment() {

//    private var _binding: FragmentMediaBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var tabMediator: TabLayoutMediator
    private val viewModelPlaylists: PlaylistsViewModel by inject()
    private val viewModelFavorites: FavoritesViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        _binding = FragmentMediaBinding.inflate(inflater, container, false)
//        return binding.root
        return ComposeView(requireContext()).apply {
            setContent {

                val playlistState = viewModelPlaylists.observeState().observeAsState()
                val favoritesState = viewModelFavorites.observeState().observeAsState()

                ThemeProject {
                    MediaScreen(
                        playlistState = playlistState as PlaylistState,
                        favoritesState = favoritesState as FavoriteState,
                        onClickOpenPlayer = { track ->
                            startActivityPlayer(track)
                        },
                        onClickVewDetails = { id ->
                            startActivityInfo(id)
                        },
                        onClickNewPlaylist = {
                            addNewPlayList()
                        }
                    )
                }
            }
        }

    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.viewPager.adapter = MediaAdapter(childFragmentManager, lifecycle)
//
//        tabMediator = TabLayoutMediator(binding.mediaTabLayout, binding.viewPager) {
//                tab, position ->
//            when(position){
//                0 -> tab.text = getString(R.string.select_tracks)
//                1 -> tab.text = getString(R.string.playlists)
//            }
//        }
//        tabMediator.attach()
//    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        tabMediator.detach()
//        _binding = null
//    }

    private fun startActivityPlayer(trackClicked: Track) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playerFragment,
            PlayerFragment.createArgs(trackClicked)
        )
    }
    private fun startActivityInfo(id: Int) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistInfoFragment,
            PlaylistInfoFragment.createArgs(id)
        )
    }

    private fun addNewPlayList(){
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistAddFragment,
            PlaylistAddFragment.createArgs(-1)
        )
    }

}