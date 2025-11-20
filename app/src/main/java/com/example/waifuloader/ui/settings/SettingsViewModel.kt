package com.example.waifuloader.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waifuloader.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel : ViewModel() {

    // Definition of all available tags in the app
    private val availableVersatileTags = listOf(
        "maid", "waifu", "marin-kitagawa", "mori-calliope",
        "raiden-shogun", "oppai", "selfies", "uniform", "kamisato-ayaka"
    )
    private val availableNsfwTags = listOf(
        "ass", "hentai", "milf", "oral", "paizuri", "ecchi", "ero"
    )

    // Subscribe to UserPreferences and map the strings to UI TagOption objects
    val uiState: StateFlow<SettingsUiState> = UserPreferences.enabledTags
        .map { enabledSet ->
            SettingsUiState(
                isDarkMode = false, // Theme logic can be added here similarly
                versatileTags = availableVersatileTags.map { name ->
                    TagOption(name, isSelected = enabledSet.contains(name))
                },
                nsfwTags = availableNsfwTags.map { name ->
                    TagOption(name, isSelected = enabledSet.contains(name))
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    fun toggleTheme() {
        // Theme toggle logic
    }

    // Forward the toggle action to the shared UserPreferences
    fun toggleTag(tagName: String) {
        UserPreferences.toggleTag(tagName)
    }
}