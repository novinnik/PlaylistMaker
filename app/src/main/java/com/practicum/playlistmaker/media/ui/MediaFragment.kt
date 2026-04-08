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
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.favorites.model.FavoriteState
import com.practicum.playlistmaker.media.favorites.ui.view_model.FavoritesViewModel
import com.practicum.playlistmaker.media.playlists.model.PlaylistState
import com.practicum.playlistmaker.media.playlists.ui.activity.PlaylistAddFragment
import com.practicum.playlistmaker.media.playlists.ui.activity.PlaylistInfoFragment
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.ui.theme.ThemeProject
import org.koin.android.ext.android.inject
import kotlin.getValue

class MediaFragment: Fragment() {

    private val viewModelPlaylists: PlaylistsViewModel by inject()
    private val viewModelFavorites: FavoritesViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val playlistState by viewModelPlaylists.observeState().observeAsState(PlaylistState.Loading)
                val favoritesState by viewModelFavorites.observeState().observeAsState(FavoriteState.Loading)

                ThemeProject {
                    MediaScreen(
                        playlistState = playlistState,
                        favoritesState = favoritesState,
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