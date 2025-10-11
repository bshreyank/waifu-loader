package com.example.waifuloader.ui.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onThemeToggle: () -> Unit,
    onTagToggle: (String) -> Unit
) {
    val backgroundColor = if (uiState.isDarkMode) Color(0xFF0A0A0A) else Color(0xFFFAFAFA)
    val surfaceColor = if (uiState.isDarkMode) Color(0xFF1A1A1A) else Color.White

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            // Header
            item {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (uiState.isDarkMode) Color.White else Color(0xFF1A1A1A)
                    )
                    Text(
                        "Customize your experience",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (uiState.isDarkMode) Color(0xFFB0B0B0) else Color(0xFF666666)
                    )
                }
            }

            // Theme Card
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = surfaceColor
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = if (uiState.isDarkMode) 0.dp else 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = if (uiState.isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                                    contentDescription = null,
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        "Appearance",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = if (uiState.isDarkMode) Color.White else Color(0xFF1A1A1A)
                                    )
                                    Text(
                                        if (uiState.isDarkMode) "Dark mode" else "Light mode",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (uiState.isDarkMode) Color(0xFFB0B0B0) else Color(0xFF666666)
                                    )
                                }
                            }

                            AnimatedSwitch(
                                checked = uiState.isDarkMode,
                                onCheckedChange = { onThemeToggle() }
                            )
                        }
                    }
                }
            }

            // Versatile Tags Card
            item {
                TagSection(
                    title = "Versatile Tags",
                    subtitle = "Select your preferred styles",
                    tags = uiState.versatileTags,
                    isDarkMode = uiState.isDarkMode,
                    surfaceColor = surfaceColor,
                    onTagToggle = onTagToggle
                )
            }

            // NSFW Tags Card
            item {
                TagSection(
                    title = "NSFW Tags",
                    subtitle = "Adult content preferences",
                    tags = uiState.nsfwTags,
                    isDarkMode = uiState.isDarkMode,
                    surfaceColor = surfaceColor,
                    onTagToggle = onTagToggle
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagSection(
    title: String,
    subtitle: String,
    tags: List<TagOption>,
    isDarkMode: Boolean,
    surfaceColor: Color,
    onTagToggle: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isDarkMode) 0.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (isDarkMode) Color.White else Color(0xFF1A1A1A)
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDarkMode) Color(0xFFB0B0B0) else Color(0xFF666666)
                )
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    AnimatedTagChip(
                        tag = tag,
                        isDarkMode = isDarkMode,
                        onToggle = { onTagToggle(tag.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedTagChip(
    tag: TagOption,
    isDarkMode: Boolean,
    onToggle: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (tag.isSelected) 1.0f else 0.95f,
        animationSpec = tween(200),
        label = "scale"
    )

    val containerColor by animateColorAsState(
        targetValue = when {
            tag.isSelected -> Color(0xFF8B5CF6)
            isDarkMode -> Color(0xFF2A2A2A)
            else -> Color(0xFFF5F5F5)
        },
        animationSpec = tween(200),
        label = "containerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            tag.isSelected -> Color.White
            isDarkMode -> Color(0xFFE0E0E0)
            else -> Color(0xFF3A3A3A)
        },
        animationSpec = tween(200),
        label = "contentColor"
    )

    FilterChip(
        selected = tag.isSelected,
        onClick = onToggle,
        label = {
            Text(
                tag.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (tag.isSelected) FontWeight.Medium else FontWeight.Normal
                ),
                color = contentColor
            )
        },
        modifier = Modifier.scale(scale),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = containerColor,
            selectedContainerColor = containerColor,
            labelColor = contentColor,
            selectedLabelColor = contentColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = tag.isSelected,
            borderColor = if (tag.isSelected) Color.Transparent else
                if (isDarkMode) Color(0xFF3A3A3A) else Color(0xFFE0E0E0),
            selectedBorderColor = Color.Transparent,
            borderWidth = 1.dp
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun AnimatedSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = Color(0xFF8B5CF6),
            checkedBorderColor = Color(0xFF8B5CF6),
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = Color(0xFFE0E0E0),
            uncheckedBorderColor = Color(0xFFE0E0E0)
        )
    )
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
                TagOption("uniform"),
                TagOption("elf", true),
                TagOption("marin-kitagawa"),
                TagOption("selfies")
            ),
            nsfwTags = listOf(
                TagOption("hentai"),
                TagOption("ecchi", true),
                TagOption("oppai"),
                TagOption("ero", true)
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
                TagOption("uniform"),
                TagOption("elf", true),
                TagOption("marin-kitagawa"),
                TagOption("selfies")
            ),
            nsfwTags = listOf(
                TagOption("hentai"),
                TagOption("ecchi", true),
                TagOption("oppai"),
                TagOption("ero", true)
            )
        ),
        onThemeToggle = {},
        onTagToggle = {}
    )
}