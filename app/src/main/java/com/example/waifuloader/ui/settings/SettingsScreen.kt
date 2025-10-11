package com.example.waifuloader.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.foundation.layout.FlowRow

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    SettingsScreen(
        uiState = uiState,
        onThemeToggle = viewModel::toggleTheme,
        onTagToggle = viewModel::toggleTag
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onThemeToggle: () -> Unit,
    onTagToggle: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (uiState.isDarkMode) Color(0xFF0D0D0D) else Color(0xFFF8F8F8)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            /** Theme Section **/
            item {
                Text(
                    "Appearance",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (uiState.isDarkMode) Color.White else Color.Black
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Dark Mode",
                        color = if (uiState.isDarkMode) Color.White else Color.Black
                    )
                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = { onThemeToggle() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF8B5CF6)
                        )
                    )
                }
            }

            /** Versatile Tags **/
            item {
                Text(
                    "Versatile Tags",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (uiState.isDarkMode) Color.White else Color.Black
                )
                Spacer(Modifier.height(8.dp))
                TagGrid(
                    tags = uiState.versatileTags,
                    isDarkMode = uiState.isDarkMode,
                    onTagToggle = onTagToggle
                )
            }

            /** NSFW Tags **/
            item {
                Text(
                    "NSFW Tags",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (uiState.isDarkMode) Color.White else Color.Black
                )
                Spacer(Modifier.height(8.dp))
                TagGrid(
                    tags = uiState.nsfwTags,
                    isDarkMode = uiState.isDarkMode,
                    onTagToggle = onTagToggle
                )
            }
        }
    }
}

@Composable
private fun TagGrid(
    tags: List<TagOption>,
    isDarkMode: Boolean,
    onTagToggle: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            FilterChip(
                selected = tag.isSelected,
                onClick = { onTagToggle(tag.name) },
                label = {
                    Text(
                        tag.name.replaceFirstChar { it.uppercase() },
                        color = if (tag.isSelected)
                            Color.White else if (isDarkMode) Color.LightGray else Color.DarkGray
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = if (isDarkMode) Color(0xFF1C1C1C) else Color(0xFFF0F0F0),
                    selectedContainerColor = Color(0xFF8B5CF6)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview_Light() {
    SettingsScreen(
        uiState = SettingsUiState(
            isDarkMode = false,
            versatileTags = listOf(
                TagOption("maid"),
                TagOption("waifu", true),
                TagOption("uniform")
            ),
            nsfwTags = listOf(
                TagOption("hentai"),
                TagOption("ecchi", true)
            )
        ),
        onThemeToggle = {},
        onTagToggle = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview_Dark() {
    SettingsScreen(
        uiState = SettingsUiState(
            isDarkMode = true,
            versatileTags = listOf(
                TagOption("maid"),
                TagOption("waifu", true),
                TagOption("uniform")
            ),
            nsfwTags = listOf(
                TagOption("hentai"),
                TagOption("ecchi", true)
            )
        ),
        onThemeToggle = {},
        onTagToggle = {}
    )
}
