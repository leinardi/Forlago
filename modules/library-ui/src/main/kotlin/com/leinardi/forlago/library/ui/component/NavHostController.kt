package com.leinardi.forlago.library.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.leinardi.forlago.library.navigation.NavigationDestination

val LocalNavHostController: ProvidableCompositionLocal<NavHostController> = compositionLocalOf { error("No NavHostController provided") }

fun <T> NavHostController.setResult(key: String, value: T): Boolean = previousBackStackEntry?.run { savedStateHandle.set(key, value) } != null

@Composable
fun <T : NavigationDestination.Result> NavHostController.observeResult(key: String, onResult: (T) -> Unit): Boolean =
    currentBackStackEntry?.let { entry ->
        entry.savedStateHandle.getLiveData<T>(key).observe(LocalLifecycleOwner.current) { result ->
            if (!result.consumed) {
                result.consumed = true
                onResult(result)
            }
        }
    } != null
