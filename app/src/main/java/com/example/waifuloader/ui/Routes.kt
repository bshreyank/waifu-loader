package com.example.waifuloader.ui

import kotlinx.serialization.Serializable

//Top level routes
@Serializable
data object HomeRoute

@Serializable
data object SavedWaifuGridRoute

@Serializable
data class SavedWaifuRoute(val id: String)

@Serializable
data object SettingsRoute