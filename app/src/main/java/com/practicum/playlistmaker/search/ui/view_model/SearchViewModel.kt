package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksSearchInteractor
import com.practicum.playlistmaker.search.domain.models.ResultSearch
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.model.TracksState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
                      private val searchInteractor:TracksSearchInteractor,
                      private val historyInteractor: HistoryInteractor
    ): ViewModel() {

    private var searchJob: Job? = null
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

    fun renderState(state: TracksState){
        stateLiveData.postValue(state)
    }

    private fun searchTrack(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)
        }

        viewModelScope.launch {
            searchInteractor
                .searchTracks(newSearchText)
                .collect { tracksSearch->
                    processResult(tracksSearch)
                }
        }
    }

    fun debounceSearchTrack(changeText: String){

        if (lastSearchText == changeText) {
            return
        }

        this.lastSearchText = changeText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTrack(changeText)
        }
    }

    private fun processResult(tracksSearch: ResultSearch) {

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

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        searchJob = null
    }

    fun clearHistory() {
        historyInteractor.clearHistory(historyInteractor.getHistory())
        initHistory()
    }

    fun addHistory(track: Track){
        historyInteractor.addToHistory(track)
        initHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}