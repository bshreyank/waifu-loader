package com.example.waifuloader.data

import com.example.waifuloader.data.models.Waifu
import com.example.waifuloader.data.models.NetworkResult

interface WaifuRepository {
    suspend fun getWaifuInfo(tags: List<String>): NetworkResult<Waifu>
}