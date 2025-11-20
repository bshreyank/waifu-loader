package com.example.waifuloader.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.waifuloader.ui.home.HomeRoute
import com.example.waifuloader.ui.saved.SavedWaifuGridRoute
import com.example.waifuloader.ui.saved.SavedWaifuRoute
import com.example.waifuloader.ui.settings.SettingsRoute


val LocalWaifuStore = staticCompositionLocalOf<WaifuStore> { error("No Waifu store provided.") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaifuApp() {
    val navController = rememberNavController()
    val waifuStore = remember { WaifuStore }

    Scaffold { innerPadding ->
        CompositionLocalProvider(LocalWaifuStore provides waifuStore) {
            NavHost(
                navController = navController,
                startDestination = HomeRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                // 🏠 Home
                composable<HomeRoute> {
                    HomeRoute(
                        onNavigateToSaved = {
                            navController.navigate(SavedWaifuGridRoute)
                        },
                        onNavigateToSettings = {
                            navController.navigate(SettingsRoute)
                        }
                    )
                }

                // 🗂️ Saved Grid
                composable<SavedWaifuGridRoute> {
                    SavedWaifuGridRoute(
                        onWaifuClick = { id ->
                            navController.navigate(SavedWaifuRoute(id = id))
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 🖼️ Saved Detail
                composable<SavedWaifuRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<SavedWaifuRoute>()

                    SavedWaifuRoute(
                        id = route.id,
                        onBackClick = { navController.popBackStack() },
                    )
                }

                composable<SettingsRoute>{
                    SettingsRoute(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}