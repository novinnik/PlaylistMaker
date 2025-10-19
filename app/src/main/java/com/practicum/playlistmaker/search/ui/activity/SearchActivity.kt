package com.practicum.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.model.TracksState
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var viewModel: SearchViewModel? = null
    private var isClickAllowed = true
    private var simpleTextWatcher: TextWatcher? = null
    private val searchHandler = Handler(Looper.getMainLooper())
    private var trackList = arrayListOf<Track>()

    enum class StateSearch {
        CLEAR,    //очистка
        ERROR,    //ошибка
        EMPTY,    //ни чего, пусто
        SUCCESS   //успешно
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historyList = arrayListOf<Track>()

        viewModel = ViewModelProvider(this, SearchViewModel.getFactory())
            .get(SearchViewModel::class.java)

        viewModel?.observeState()?.observe(this) { render(it) }

        viewModel?.observeHistory()?.observe(this) {
            historyList.clear()
            historyList.addAll(it)
        }

        binding.toolbarActivitySearch.setNavigationOnClickListener { finish() }

        binding.searchHistoryButtonClear.setOnClickListener { clearHistorySearch() }

        binding.btPlaceholderUpdate.setOnClickListener {
            updateUI(StateSearch.CLEAR)
            viewModel?.debounceSearchTrack(binding.inputSearchText.text.toString())
        }

        binding.clearButtonSearch.setOnClickListener {

            binding.inputSearchText.setText(TEXT_EMPTY)

            trackList.clear()
            binding.trackSearchRecycler.adapter?.notifyDataSetChanged()

            updateUI(StateSearch.CLEAR)
            val inputMM = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
                    viewModel?.debounceSearchTrack(changeText = s?.toString() ?: "")
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
                    viewModel?.debounceSearchTrack(binding.inputSearchText.text.toString())
                }
            }
            false
        }

        binding.trackSearchRecycler.adapter = TrackAdapter(trackList) { track ->
          //  if (clickDebounce()) {
                viewModel?.addHistory(track)
                startActivityPlayer(track)
           // }

        }

        binding.searchHistoryRecyclerView.adapter = TrackAdapter(historyList) { track ->
            //if (clickDebounce()) {
                viewModel?.addHistory(track)
                startActivityPlayer(track)
            //}
        }


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
        viewModel?.clearHistory()
        binding.searchHistoryRecyclerView.adapter?.notifyDataSetChanged()
        binding.searchHistoryLayout.visibility = View.GONE
//        updateHistorySearch()
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
        if (clickDebounce()) {
            val mediaIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
            mediaIntent.putExtra(MEDIA_TRACK_KEY, trackClicked)
            startActivity(mediaIntent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            searchHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        searchHandler.removeCallbacksAndMessages(null)
        simpleTextWatcher.let { binding.inputSearchText.removeTextChangedListener(it) }
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


    companion object {
        const val TEXT_EMPTY = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val MEDIA_TRACK_KEY = "media_track_key"
        const val SEARCH_HISTORY_PREF = "search_history_pref"
    }
}