package com.leinardi.forlago.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leinardi.forlago.library.feature.Feature
import com.leinardi.forlago.library.ui.component.LocalMainNavigationBarItemColors
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun MainNavigationBar(
    navController: NavHostController,
    mainNavigationBarEntries: ImmutableMap<String, Feature.NavigationBarEntry>,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    itemColors: NavigationBarItemColors = NavigationBarItemDefaults.colors(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.run { destination.route }
    val hideBottomNav = mainNavigationBarEntries[currentRoute]?.hideBottomNav != false

    if (!hideBottomNav) {
        NavigationBar(
            containerColor = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
        ) {
            CompositionLocalProvider(
                LocalMainNavigationBarItemColors provides itemColors,
            ) {
                mainNavigationBarEntries.values.forEach { entry ->
                    entry.item(this, currentRoute == entry.route) {
                        navController.navigate(entry.route) {
                            if (!entry.hideBottomNav) {
                                restoreState = true
                                popUpTo(0) {
                                    inclusive = true
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
        }
    }
}
