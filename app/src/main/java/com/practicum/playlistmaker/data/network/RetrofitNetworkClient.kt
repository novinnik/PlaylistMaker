package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.NetworkResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(private val service: ITunesApiService) : NetworkClient {

    override fun doRequest(dto: Any): NetworkResponse {
        try{
            if (dto is TracksSearchRequest) {

                val resp = service.search(dto.expression).execute()

                return resp.body()?.apply { resultCode = 200 } ?: NetworkResponse().apply { resultCode = 400 }

            } else {
                NetworkResponse().apply { resultCode = 400 }
            }
        }
        catch (ex: Exception){
            return NetworkResponse().apply { resultCode = 500 }
        }
        return NetworkResponse().apply { resultCode = -1 }
    }
}