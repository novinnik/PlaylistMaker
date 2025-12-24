package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.NetworkResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val service: ITunesApiService): NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResponse {

        return withContext(Dispatchers.IO) {
            try {
                if (dto is TracksSearchRequest) {
                    val resp = service.search(dto.expression)
                    resp.apply { resultCode = if (!resp.results.isEmpty()) 200 else 404}
                } else {
                    NetworkResponse().apply { resultCode = 400 }
                }
            } catch (ex: Throwable) {
                NetworkResponse().apply { resultCode = 500 }
            }
        }
    }

}