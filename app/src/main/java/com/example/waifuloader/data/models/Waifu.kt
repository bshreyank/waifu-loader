package com.example.waifuloader.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Waifu(
    @SerialName("image_id") val id: String = "",
    val url: String = "",
    val tags: List<String> = emptyList()
)
