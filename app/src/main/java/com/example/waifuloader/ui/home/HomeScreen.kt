@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.waifuloader.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.waifuloader.R
import com.example.waifuloader.data.models.Waifu
import com.example.waifuloader.ui.LocalWaifuStore
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSaved: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val waifuStore = LocalWaifuStore.current
    val currentWaifu by waifuStore.currentWaifu.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState,
        currentWaifu = currentWaifu,
        onGetWaifu = { viewModel.getWaifu() },
        onNewWaifuLoaded = { waifu -> waifuStore.setCurrentWaifu(waifu) },
        onNavigateToSaved = onNavigateToSaved,
        onNavigateToSettings = onNavigateToSettings,
        onSaveWaifu = { waifu -> waifuStore.saveWaifu(waifu) }
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    currentWaifu: Waifu,
    onGetWaifu: () -> Unit,
    onNewWaifuLoaded: (Waifu) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSaved: () -> Unit,
    onSaveWaifu: (Waifu) -> Unit,
) {
    var TAG = "HomeScreen"
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Use current waifu if already set, otherwise from uiState
    val waifuToShow = if (currentWaifu.id.isNotEmpty()) currentWaifu else uiState.currentWaifu

    val imageLoader = rememberAsyncImagePainter(
        model = waifuToShow.url,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    // Only fetch waifu if there's no current one in the store
    LaunchedEffect(Unit) {
        if (currentWaifu.id.isEmpty()) {
            onGetWaifu()
        }
    }

    // When new waifu is loaded, update the store
    LaunchedEffect(uiState.currentWaifu) {
        if (uiState.currentWaifu.id.isNotEmpty() && uiState.currentWaifu != currentWaifu) {
            onNewWaifuLoaded(uiState.currentWaifu)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Waifu Loader") },
                actions = {
                    IconButton(onClick = onNavigateToSaved) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Saved Waifus"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = if (!isError) imageLoader else painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Loaded image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(onClick = {
                    onSaveWaifu(waifuToShow)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Waifu Saved.. <3!",
                            withDismissAction = true
                        )
                    }
                }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Save image")
                }

                FloatingActionButton(onClick = {
                    onGetWaifu()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next image")
                }

                FloatingActionButton(onClick = {
                    Log.d(TAG, " Navigated to Settings Page.")
                    onNavigateToSettings()
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings page.")
                }
            }
        }
    }
}