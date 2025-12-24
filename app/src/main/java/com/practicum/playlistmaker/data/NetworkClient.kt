package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.NetworkResponse

interface NetworkClient {
    suspend fun doRequest(dto:Any): NetworkResponse
}