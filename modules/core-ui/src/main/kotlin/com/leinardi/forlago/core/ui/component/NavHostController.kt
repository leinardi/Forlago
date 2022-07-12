package com.leinardi.forlago.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.leinardi.forlago.core.navigation.NavigationDestination

val LocalNavHostController: ProvidableCompositionLocal<NavHostController> = compositionLocalOf { error("No NavHostController provided") }

fun <T> NavHostController.setResult(key: String, value: T): Boolean = previousBackStackEntry?.run { savedStateHandle.set(key, value) } != null

@Composable
fun <T : NavigationDestination.Result> NavHostController.observeResult(key: String, onResult: (T) -> Unit): Boolean =
    currentBackStackEntry?.let { entry ->
        entry.savedStateHandle.getLiveData<T>(key).observeAsState().value?.let { result ->
            if (!result.consumed) {
                result.consumed = true
                onResult(result)
            }
        }
    } != null
