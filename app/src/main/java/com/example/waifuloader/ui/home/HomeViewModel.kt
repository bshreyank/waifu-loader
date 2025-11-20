package com.example.waifuloader.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waifuloader.data.UserPreferences
import com.example.waifuloader.data.WaifuRepository
import com.example.waifuloader.data.models.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val waifuRepository: WaifuRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun getWaifu() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // 1. Get the list of all enabled tags
            val allEnabledTags = UserPreferences.enabledTags.value.toList()

            // 2. CRITICAL FIX: Pick ONE random tag to avoid the "AND" strict filtering.
            // If the list is empty, we send an empty list (API returns random image).
            val searchTags = if (allEnabledTags.isNotEmpty()) {
                listOf(allEnabledTags.random())
            } else {
                emptyList()
            }

            Log.d(TAG, "Fetching waifu using random tag from preferences: $searchTags")

            val result = waifuRepository.getWaifuInfo(tags = searchTags)

            when (result) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "Image loaded. Tags: ${result.data.tags}")
                    _uiState.update { it.copy(currentWaifu = result.data) }
                }

                is NetworkResult.Error -> {
                    Log.d(TAG, "Error: ${result.message} code: ${result.code}")
                    // If error is 404, it might be a rare tag with no images.
                    // You could recursively call getWaifu() here to try another tag automatically.
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}