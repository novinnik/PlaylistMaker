package com.practicum.playlistmaker.media.favorites.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.favorites.model.FavoriteState
import com.practicum.playlistmaker.media.favorites.ui.view_model.FavoritesViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by inject()
    private var favoritesList = arrayListOf<Track>()
    private lateinit var onClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner){render(it)}

        onClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false){
                track -> startActivityPlayer(track)
        }

        binding.favoritesRecycler.adapter = TrackAdapter(favoritesList){track ->
            onClickDebounce(track)
        }

       viewModel.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.favoritesRecycler.adapter = null
        _binding = null
    }

    private fun startActivityPlayer(trackClicked: Track) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playerFragment,
            PlayerFragment.createArgs(trackClicked)
        )
    }
    fun render(state: FavoriteState){
        when(state){
            is FavoriteState.Loading ->showLoading()
            is FavoriteState.Content -> showContent(state.tracks)
            is FavoriteState.Empty -> showEmpty()
        }
    }

    fun showLoading() {
        binding.apply {
            favoritesProgressBar.visibility = View.VISIBLE
            errorImageFragmentFavorites.visibility = View.GONE
            errorMessageFragmentFavorites.visibility = View.GONE
            favoritesRecycler.visibility = View.GONE
        }
    }

    fun showContent(tracks: List<Track>) {
        binding.apply {
            favoritesProgressBar.visibility = View.GONE
            errorImageFragmentFavorites.visibility = View.GONE
            errorMessageFragmentFavorites.visibility = View.GONE
            favoritesRecycler.visibility = View.VISIBLE
        }
        favoritesList.clear()
        favoritesList.addAll(tracks)
        binding.favoritesRecycler.adapter?.notifyDataSetChanged()
    }

    fun showEmpty() {
        binding.apply {
            favoritesProgressBar.visibility = View.GONE
            errorImageFragmentFavorites.visibility = View.VISIBLE
            errorMessageFragmentFavorites.visibility = View.VISIBLE
            favoritesRecycler.visibility = View.GONE
        }
        favoritesList.clear()
        binding.favoritesRecycler.adapter?.notifyDataSetChanged()
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = FavoritesFragment()
    }
}