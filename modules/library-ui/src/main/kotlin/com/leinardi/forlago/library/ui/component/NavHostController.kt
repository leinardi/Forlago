/*
 * Copyright 2023 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leinardi.forlago.library.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.leinardi.forlago.library.navigation.api.destination.NavigationDestination

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
