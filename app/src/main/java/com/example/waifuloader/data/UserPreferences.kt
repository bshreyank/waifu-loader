package com.example.waifuloader.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * A singleton object to hold the user's tag preferences.
 * This acts as the bridge between Settings (writing) and Home (reading).
 */
object UserPreferences {
    // Default enabled tags
    private val _enabledTags = MutableStateFlow(setOf("maid", "waifu"))
    val enabledTags = _enabledTags.asStateFlow()

    fun toggleTag(tag: String) {
        _enabledTags.update { currentTags ->
            if (currentTags.contains(tag)) {
                currentTags - tag
            } else {
                currentTags + tag
            }
        }
    }
}