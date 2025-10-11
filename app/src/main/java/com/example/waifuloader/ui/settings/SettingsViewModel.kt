package com.example.waifuloader.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            isDarkMode = false,
            versatileTags = listOf(
                TagOption("maid"),
                TagOption("waifu"),
                TagOption("marin-kitagawa"),
                TagOption("mori-calliope"),
                TagOption("raiden-shogun"),
                TagOption("oppai"),
                TagOption("selfies"),
                TagOption("uniform"),
                TagOption("kamisato-ayaka")
            ),
            nsfwTags = listOf(
                TagOption("ass"),
                TagOption("hentai"),
                TagOption("milf"),
                TagOption("oral"),
                TagOption("paizuri"),
                TagOption("ecchi"),
                TagOption("ero")
            )
        )
    )

    val uiState: StateFlow<SettingsUiState> = _uiState

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun toggleTag(tagName: String) {
        _uiState.update { state ->
            val isVersatile = state.versatileTags.any { it.name == tagName }
            if (isVersatile) {
                state.copy(
                    versatileTags = state.versatileTags.map {
                        if (it.name == tagName) it.copy(isSelected = !it.isSelected) else it
                    }
                )
            } else {
                state.copy(
                    nsfwTags = state.nsfwTags.map {
                        if (it.name == tagName) it.copy(isSelected = !it.isSelected) else it
                    }
                )
            }
        }
    }
}
