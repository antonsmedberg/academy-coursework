package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.di.ServiceLocator
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.components.SimpleTopBar
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main.FormScreen
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main.MainViewModel
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main.SavedScreen

sealed class MainDest(val route: String, val label: String) {
    data object Form : MainDest("form", "Formulär")
    data object Saved : MainDest("saved", "Sparat")
}

private const val MAIN_GRAPH_ROUTE = "main"

@Composable
fun MainNavRoot(onLogout: () -> Unit) {
    val nav = rememberNavController()
    val items = listOf(MainDest.Form, MainDest.Saved)
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination

    Scaffold(
        topBar = { SimpleTopBar(title = "LifeCycle v5", onLogout = onLogout) },
        bottomBar = {
            NavigationBar {
                items.forEach { dest ->
                    val selected = currentDest?.hierarchy?.any { it.route == dest.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            nav.navigate(dest.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            }
                        },
                        label = { Text(dest.label) },
                        icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = null) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = MainDest.Saved.route,   // <— här
            route = MAIN_GRAPH_ROUTE,
            modifier = Modifier.padding(padding)
        ) {
            composable(MainDest.Form.route) { entry ->
                val parentEntry = remember(entry) { nav.getBackStackEntry(MAIN_GRAPH_ROUTE) }
                val vm: MainViewModel =
                    viewModel(parentEntry, factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return MainViewModel(
                                ServiceLocator.sessionPrefs,
                                ServiceLocator.profileRepo
                            ) as T
                        }
                    })
                FormScreen(
                    vm = vm,
                    onSaved = {
                        nav.navigate(MainDest.Saved.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                        }
                    }
                )
            }
            composable(MainDest.Saved.route) { entry ->
                val parentEntry = remember(entry) { nav.getBackStackEntry(MAIN_GRAPH_ROUTE) }
                val vm: MainViewModel =
                    viewModel(parentEntry, factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return MainViewModel(
                                ServiceLocator.sessionPrefs,
                                ServiceLocator.profileRepo
                            ) as T
                        }
                    })
                SavedScreen(
                    vm = vm,
                    onGoToForm = {
                        nav.navigate(MainDest.Form.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                        }
                    }
                )
            }
        }
    }
}
