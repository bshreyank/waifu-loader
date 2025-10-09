package com.example.waifuloader.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waifuloader.data.WaifuRepository
import com.example.waifuloader.data.models.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeUiState(
    val isLoading: Boolean = false,
    val imageUrl: String? = null,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val waifuRepository: WaifuRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun getWaifu() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = waifuRepository.getWaifuInfo()

            when (result) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "image data: ${result.data}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            imageUrl = result.data.url,
                            error = null
                        )
                    }
                }

                is NetworkResult.Error -> {
                    Log.d(TAG, "error: ${result.message} code: ${result.code}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}