package com.practicum.playlistmaker.media.favorites.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.favorites.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.media.favorites.model.FavoriteState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>(FavoriteState.Loading)
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun fillData(){
        stateLiveData.postValue(FavoriteState.Loading)
        viewModelScope.launch {
            favoritesInteractor
                .getFavorites()
                .collect { value ->
                    processResult(value)
                }
        }
    }

    private fun processResult(tracks: List<Track>){
        if (tracks.isNotEmpty()){
            val arrayListTracks = ArrayList(tracks)
            renderStare(FavoriteState.Content(arrayListTracks))
        } else {
            renderStare(FavoriteState.Empty)
        }
    }
    private fun renderStare(state: FavoriteState){
        stateLiveData.postValue(state)
    }

}