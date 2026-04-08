package com.practicum.playlistmaker.search.ui.activity

import SearchScreen
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.model.TracksState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.theme.ThemeProject
import com.practicum.playlistmaker.util.ConnectionReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment: Fragment() {

    private val viewModel by viewModel<SearchViewModel>()
    private val connectionBroadcastReceiver = ConnectionReceiver()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                var changeText by rememberSaveable { mutableStateOf("")  }
                ThemeProject {
                    SearchScreen(
                        viewModel = viewModel,
                        textSearch = changeText,
                        onTextChanged = { newText ->
                            changeText = newText
                            viewModel.debounceSearchTrack(newText)
                        },
                        onClearClick = {
                            changeText = ""
                            viewModel.renderState(TracksState.Content(arrayListOf()))},
                        onTrackClick = { track ->
                            startActivityPlayer(track)
                        },
                        onClearHistory =  { viewModel.clearHistory() },
                        onHistoryTrackClick = {track ->
                            startActivityPlayer(track)
                        },
                        onRetry = {
                            viewModel.debounceSearchTrack(changeText)
                        }
                    )
                }

            }
        }
    }


    private fun startActivityPlayer(trackClicked: Track) {
        viewModel.addHistory(trackClicked)
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(trackClicked)
        )
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            connectionBroadcastReceiver,
            IntentFilter(ConnectionReceiver.ACTION_CONNECTIVITY),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(connectionBroadcastReceiver)
    }

}
