/*
 * Copyright 2024 Roberto Leinardi.
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

package com.leinardi.forlago.navigation

import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.leinardi.forlago.di.AppEntryPoints
import com.leinardi.forlago.library.android.initializer.ContextProvider
import dagger.hilt.EntryPoints

fun NavGraphBuilder.addComposableDestinations() {
    getFeatures().forEach { feature ->
        feature.composableDestinations.forEach { entry ->
            val destination = entry.key
            composable(destination.route, destination.arguments, destination.deepLinks) { entry.value() }
        }
    }
}

fun NavGraphBuilder.addDialogDestinations() {
    getFeatures().forEach { feature ->
        feature.dialogDestinations.forEach { entry ->
            val destination = entry.key
            dialog(destination.route, destination.arguments, destination.deepLinks) { entry.value() }
        }
    }
}

fun NavGraphBuilder.addBottomSheetDestinations() {
    getFeatures().forEach { feature ->
        feature.bottomSheetDestinations.forEach { entry ->
            val destination = entry.key
            bottomSheet(destination.route, destination.arguments, destination.deepLinks) {
                // Remove CompositionLocalProvider after migration to Material 3 Navigation
                CompositionLocalProvider(LocalContentColor provides contentColorFor(BottomSheetDefaults.ContainerColor)) {
                    entry.value()
                }
            }
        }
    }
}

private fun getFeatures() = EntryPoints.get(ContextProvider.applicationContext, AppEntryPoints.FeatureManagerInterface::class.java)
    .getFeatureManager()
    .features
