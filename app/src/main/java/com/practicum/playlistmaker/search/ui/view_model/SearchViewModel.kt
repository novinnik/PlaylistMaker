package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksSearchInteractor
import com.practicum.playlistmaker.search.domain.models.ResultSearch
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.model.TracksState

class SearchViewModel(
                      private val searchInteractor:TracksSearchInteractor,
                      private val historyInteractor: HistoryInteractor
    ): ViewModel() {

    private val searchHandler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable{
        val newSearchText = lastSearchText?:""
        searchTrack(newSearchText)
    }

    private var lastSearchText: String? = null
    private var stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    private val historyLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeHistory(): LiveData<ArrayList<Track>> = historyLiveData

    init {
        initHistory()
    }

    fun initHistory(){
        historyLiveData.value = historyInteractor.getHistory()
    }

    private val consumer = object : TracksSearchInteractor.TracksConsumer{
        override fun consume(tracksSearch: ResultSearch){
            searchHandler.post {
                val trackList = arrayListOf<Track>()

                if (tracksSearch.resultCode == 200) {

                    if (tracksSearch.listTracks.isNotEmpty()) {
                        trackList.addAll(tracksSearch.listTracks)
                        renderState(TracksState.Content(trackList))
                    } else {
                        renderState(TracksState.Empty(true))
                    }
                } else {
                    renderState(TracksState.Error(tracksSearch.resultCode))
                }

            }
        }
    }

    fun renderState(state: TracksState){
        stateLiveData.postValue(state)
    }

    private fun searchTrack(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)
        }
        searchInteractor.searchTracks(newSearchText, consumer)
    }

    fun debounceSearchTrack(changeText: String){

        if (lastSearchText == changeText) {
            return
        }

        this.lastSearchText = changeText

        searchHandler.removeCallbacks(searchRunnable)
        searchHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onCleared() {
        super.onCleared()
        searchHandler.removeCallbacks(searchRunnable)
    }

    fun clearHistory() {
        historyInteractor.clearHistory(historyInteractor.getHistory())
        initHistory()
    }

    fun addHistory(track: Track){
        historyInteractor.addToHistory(historyInteractor.getHistory(), track)
        initHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }

}