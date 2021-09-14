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

package com.leinardi.template.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.leinardi.template.navigation.NavigatorEvent
import com.leinardi.template.navigation.TemplateNavigator
import com.leinardi.template.navigation.addComposableDestinations
import com.leinardi.template.navigation.addDialogDestinations
import com.leinardi.template.navigation.destination.foo.FooDestination
import com.leinardi.template.ui.theme.TemplateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var templateNavigator: TemplateNavigator

    lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val navHostController: NavHostController = rememberNavController().also { this.navHostController = it }
            TemplateTheme {
                TemplateScaffold(
                    navController = navHostController,
                    templateNavigator = templateNavigator,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navHostController.handleDeepLink(intent)
    }
}

@Composable
fun TemplateScaffold(navController: NavHostController, templateNavigator: TemplateNavigator) {
    LaunchedEffect(navController) {
        templateNavigator.destinations.collect {
            when (val event = it) {
                is NavigatorEvent.NavigateUp -> navController.navigateUp()
                is NavigatorEvent.Directions -> navController.navigate(
                    event.destination,
                    event.builder
                )
            }
        }
    }
    Scaffold {
        NavHost(
            navController = navController,
            startDestination = FooDestination.route(),
            builder = {
                addComposableDestinations()
                addDialogDestinations()
            }
        )
    }
}
