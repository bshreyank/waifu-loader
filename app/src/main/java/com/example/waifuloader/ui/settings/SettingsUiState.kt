package com.example.waifuloader.ui.settings

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val versatileTags: List<TagOption> = emptyList(),
    val nsfwTags: List<TagOption> = emptyList()
)

data class TagOption(
    val name: String,
    val isSelected: Boolean = false
)
