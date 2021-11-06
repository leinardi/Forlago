/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.template.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.leinardi.template.android.initializer.ContextProvider
import com.leinardi.template.di.AppEntryPoints
import dagger.hilt.EntryPoints

fun NavGraphBuilder.addComposableDestinations() {
    getFeatures().forEach { feature ->
        feature.composableDestinations.forEach { entry ->
            val destination = entry.key
            composable(destination.route(), destination.arguments, destination.deepLinks) { entry.value() }
        }
    }
}

fun NavGraphBuilder.addDialogDestinations() {
    getFeatures().forEach { feature ->
        feature.dialogDestinations.forEach { entry ->
            val destination = entry.key
            dialog(destination.route(), destination.arguments, destination.deepLinks) { entry.value() }
        }
    }
}

private fun getFeatures() = EntryPoints.get(ContextProvider.applicationContext, AppEntryPoints.FeatureManagerInterface::class.java)
    .getFeatureManager().features
