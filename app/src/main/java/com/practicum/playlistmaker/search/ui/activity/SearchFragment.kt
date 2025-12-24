package com.practicum.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.model.TracksState
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment: Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    var isClickAllowed: Boolean = true
    private var simpleTextWatcher: TextWatcher? = null
    private var trackList = arrayListOf<Track>()
    private lateinit var onClickDebounce: (Track) -> Unit

    enum class StateSearch {
        CLEAR,
        ERROR,
        EMPTY,
        SUCCESS
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isClickAllowed = true
        val historyList = arrayListOf<Track>()

        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }

        viewModel.observeHistory().observe(viewLifecycleOwner) {
            historyList.clear()
            historyList.addAll(it)
        }

        binding.searchHistoryButtonClear.setOnClickListener { clearHistorySearch() }

        binding.btPlaceholderUpdate.setOnClickListener {
            updateUI(StateSearch.CLEAR)
            viewModel.debounceSearchTrack(binding.inputSearchText.text.toString())
        }

        binding.clearButtonSearch.setOnClickListener {
            binding.inputSearchText.setText(TEXT_EMPTY)

            trackList.clear()
            viewModel.renderState(TracksState.Content(trackList))

            binding.trackSearchRecycler.adapter?.notifyDataSetChanged()

            updateUI(StateSearch.CLEAR)

            val inputMM = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMM?.hideSoftInputFromWindow(binding.inputSearchText.windowToken, 0)
        }

        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButtonSearch.visibility = clearButtonVisibility(s)

                if (binding.inputSearchText.hasFocus() && s.isNullOrEmpty() && historyList.isNotEmpty()) {
                    binding.searchHistoryLayout.visibility = View.VISIBLE
                } else {
                    binding.searchHistoryLayout.visibility = View.GONE

                    val changeText = s?.toString() ?: ""
                    if (!changeText.isEmpty()) viewModel.debounceSearchTrack(changeText)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        simpleTextWatcher?.let { binding.inputSearchText.addTextChangedListener(it) }

        binding.inputSearchText.setOnFocusChangeListener { view, hasFocus ->
            binding.searchHistoryLayout.visibility =
                if (hasFocus && binding.inputSearchText.text.isEmpty() && historyList.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        binding.inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputSearchText.text.toString().isNotEmpty()) {
                    viewModel.debounceSearchTrack(binding.inputSearchText.text.toString())
                }
            }
            false
        }

        onClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false){
            track -> startActivityPlayer(track)
        }


        binding.trackSearchRecycler.adapter = TrackAdapter(trackList) { track ->
            onClickDebounce(track)
        }

        binding.searchHistoryRecyclerView.adapter = TrackAdapter(historyList) { track ->
            onClickDebounce(track)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.trackSearchRecycler.adapter = null
        binding.searchHistoryRecyclerView.adapter = null
        simpleTextWatcher.let { binding.inputSearchText.removeTextChangedListener(it) }
        _binding = null
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearHistorySearch() {
        viewModel.clearHistory()
        binding.searchHistoryRecyclerView.adapter?.notifyDataSetChanged()
        binding.searchHistoryLayout.visibility = View.GONE
    }

    private fun updateUI(state: StateSearch) {
        when (state) {
            StateSearch.EMPTY -> {
                binding.apply {
                    trackSearchRecycler.visibility = View.GONE
                    placeholderNoConnection.visibility = View.GONE
                    placeholderError.visibility = View.VISIBLE
                    searchProgressBar.visibility = View.GONE
                }
            }

            StateSearch.ERROR -> {
                binding.apply {
                    trackSearchRecycler.visibility = View.GONE
                    placeholderNoConnection.visibility = View.VISIBLE
                    placeholderError.visibility = View.GONE
                    searchProgressBar.visibility = View.GONE
                }
            }

            StateSearch.SUCCESS -> {
                binding.apply {
                    trackSearchRecycler.visibility = View.VISIBLE
                    placeholderNoConnection.visibility = View.GONE
                    placeholderError.visibility = View.GONE
                    searchProgressBar.visibility = View.GONE
                }
            }

            StateSearch.CLEAR -> {
                binding.apply {
                    trackSearchRecycler.visibility = View.GONE
                    placeholderNoConnection.visibility = View.GONE
                    placeholderError.visibility = View.GONE
                    searchProgressBar.visibility = View.GONE
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

    fun render(state: TracksState) {
        updateUI(StateSearch.CLEAR)
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Error -> showError()
            is TracksState.Empty -> showEmpty()
        }
    }

    fun showLoading() {
        binding.searchProgressBar.visibility = View.VISIBLE
    }

    fun showContent(tracks: List<Track>) {
        trackList.clear()
        trackList.addAll(tracks)
        binding.trackSearchRecycler.adapter?.notifyDataSetChanged()
        updateUI(StateSearch.SUCCESS)
    }

    fun showError() {
        trackList.clear()
        binding.trackSearchRecycler.adapter?.notifyDataSetChanged()
        updateUI(StateSearch.ERROR)
    }

    fun showEmpty() {
        trackList.clear()
        binding.trackSearchRecycler.adapter?.notifyDataSetChanged()
        updateUI(StateSearch.EMPTY)
    }

    fun showMessageToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
    companion object {
        const val TEXT_EMPTY = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
