package com.example.waifuloader.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.waifuloader.ui.favorites.FavoritesScreen
import com.example.waifuloader.ui.home.HomeScreen
import com.example.waifuloader.ui.saved.SavedScreen
import com.example.waifuloader.ui.settings.SettingsRoute
import com.example.waifuloader.ui.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaifuApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Waifu Loader") })
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    )
    { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(navController = navController, startDestination = HomeRoute) {
                composable<HomeRoute> {
                    HomeScreen(onNavigateToSaved = {
                        navController.navigate(route = SavedRoute)
                    })
                }
                composable<SavedRoute> {
                    SavedScreen(onBack = {
                        navController.navigateUp()
                    })
                }

                composable <FavoritesRoute> {
                    FavoritesScreen()
                }

                composable<SettingsRoute> {
                    SettingsRoute()
                }
            }
        }
    }
}