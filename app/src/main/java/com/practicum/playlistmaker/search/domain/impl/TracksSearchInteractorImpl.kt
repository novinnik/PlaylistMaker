package com.practicum.playlistmaker.search.domain.impl

import java.util.concurrent.Executors
import com.practicum.playlistmaker.search.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.search.domain.api.TracksSearchInteractor


class TracksSearchInteractorImpl(private val repository: TracksSearchRepository) : TracksSearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksSearchInteractor.TracksConsumer) {
        executor.execute {consumer.consume(repository.searchTracks(expression))}
    }
}