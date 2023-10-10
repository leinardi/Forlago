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

@file:OptIn(ExperimentalFoundationApi::class)

package com.leinardi.forlago.feature.debug.ui

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.forlago.feature.debug.R
import com.leinardi.forlago.feature.debug.interactor.GetDebugInfoInteractor
import com.leinardi.forlago.feature.debug.ui.DebugContract.Event
import com.leinardi.forlago.feature.debug.ui.DebugContract.State
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugNavigationBarItem.Features
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugNavigationBarItem.Info
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugNavigationBarItem.Options
import com.leinardi.forlago.library.network.api.interactor.ReadEnvironmentInteractor.Environment
import com.leinardi.forlago.library.ui.annotation.DevicePreviews
import com.leinardi.forlago.library.ui.component.LocalMainScaffoldPadding
import com.leinardi.forlago.library.ui.component.LocalSnackbarHostState
import com.leinardi.forlago.library.ui.component.MainNavigationBarItem
import com.leinardi.forlago.library.ui.component.PreviewFeature
import com.leinardi.forlago.library.ui.component.Scaffold
import com.leinardi.forlago.library.ui.component.SettingsGroup
import com.leinardi.forlago.library.ui.component.SettingsMenuLink
import com.leinardi.forlago.library.ui.component.SettingsMenuSwitch
import com.leinardi.forlago.library.ui.component.TopAppBar
import com.leinardi.forlago.library.ui.theme.Spacing
import kotlinx.coroutines.launch

@Composable
fun DebugScreen(viewModel: DebugViewModel = hiltViewModel()) {
    DebugScreen(
        state = viewModel.viewState.value,
        sendEvent = { viewModel.onUiEvent(it) },
    )
}

@Suppress("ReusedModifierInstance")
@Composable
private fun DebugScreen(
    state: State,
    sendEvent: (event: Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        Scaffold(
            modifier = modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(LocalMainScaffoldPadding.current.value)
                .consumeWindowInsets(LocalMainScaffoldPadding.current.value),
            topBar = {
                TopAppBar(
                    title = stringResource(R.string.debug_screen),
                    onNavigateUp = { sendEvent(Event.OnUpButtonClicked) },
                    scrollBehavior = scrollBehavior,
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar {
                    state.bottomNavigationItems.forEachIndexed { index, screen ->
                        MainNavigationBarItem(
                            icon = screen.icon,
                            label = screen.label,
                            selected = state.selectedNavigationItem == state.bottomNavigationItems[index],
                            onClick = { sendEvent(Event.OnNavigationBarItemSelected(state.bottomNavigationItems[index])) },
                        )
                    }
                }
            },
        ) { scaffoldPadding ->
            when (state.selectedNavigationItem) {
                Info -> Info(
                    state = state,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(scaffoldPadding),
                )

                Options -> Options(
                    state = state,
                    sendEvent = sendEvent,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(scaffoldPadding),
                )

                Features -> Features(
                    state = state,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(scaffoldPadding),
                )
            }
        }
    }
}

@Composable
private fun Info(
    state: State,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.x02),
        ) {
            Text(
                modifier = Modifier.padding(bottom = Spacing.x01),
                text = state.debugInfo.app.name,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall,
            )
            ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.outline) {
                    Text(text = "Version name: ${state.debugInfo.app.versionName}")
                    Text(text = "Version code: ${state.debugInfo.app.versionCode}")
                    Text(text = "Application ID: ${state.debugInfo.app.packageName}")
                    Text(text = "AppUpdateInfo: ${state.appUpdateInfo}")
                }
            }
        }
        DeviceInfo(state)
    }
}

@Composable
private fun Options(
    state: State,
    sendEvent: (event: Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        SettingsGroup(
            title = { Text(text = "Environment") },
        ) {
            var expanded by remember { mutableStateOf(false) }
            // We want to react on tap/press on TextField to show menu
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.padding(horizontal = Spacing.x02),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = state.selectedEnvironment.name,
                    onValueChange = { },
                    label = { Text("Select an environment") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded,
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                ) {
                    state.environments.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.name) },
                            onClick = {
                                sendEvent(Event.OnEnvironmentSelected(selectionOption))
                                expanded = false
                            },
                        )
                    }
                }
            }
            SettingsMenuSwitch(
                title = { Text("Certificate Pinning") },
                checked = state.certificatePinningEnabled,
                onCheckedChange = {
                    sendEvent(Event.OnEnableCertificatePinning(it))
                },
                subtitle = { Text("Changing this value will restart the app") },
            )
        }
        SettingsGroup(
            title = { Text(text = "GraphQL") },
        ) {
            Button(
                onClick = { sendEvent(Event.OnClearApolloCacheClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.x02),
            ) {
                Text("Clear Apollo cache")
            }
        }
        SettingsGroup(
            title = { Text(text = "Crashlytics") },
        ) {
            Button(
                onClick = { sendEvent(Event.OnForceCrashClicked) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.x02),
            ) {
                Text("Force App crash")
            }
        }
    }
}

@Composable
private fun DeviceInfo(
    state: State,
    modifier: Modifier = Modifier,
) {
    SettingsGroup(
        title = { Text(text = stringResource(R.string.debug_device_info)) },
        modifier = modifier,
    ) {
        Column {
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.debug_manufacturer)) },
                subtitle = { Text(text = state.debugInfo.device.manufacturer) },
            )
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.debug_model)) },
                subtitle = { Text(text = state.debugInfo.device.model) },
            )
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.debug_resolution_px)) },
                subtitle = { Text(text = state.debugInfo.device.resolutionPx) },
            )
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.debug_resolution_dp)) },
                subtitle = { Text(text = state.debugInfo.device.resolutionDp) },
            )
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.debug_density)) },
                subtitle = {
                    Text(
                        text = "%sx / %sx / %s dpi".format(
                            state.debugInfo.device.density,
                            state.debugInfo.device.scaledDensity,
                            state.debugInfo.device.densityDpi,
                        ),
                    )
                },
            )
            SettingsMenuLink(
                title = { Text(text = stringResource(R.string.debug_api_level)) },
                subtitle = { Text(text = state.debugInfo.device.apiLevel.toString()) },
            )
        }
    }
}

@Composable
private fun Features(
    state: State,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { state.featureList.size }
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier,
    ) {
        PrimaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                )
            },
        ) {
            state.featureList.forEachIndexed { index, feature ->
                Tab(
                    text = { Text(feature.featureId) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                )
            }
        }
        HorizontalPager(
            state = pagerState,
        ) { page ->
            state.featureList[page].composable()
        }
    }
}

private val previewDebugInfo = GetDebugInfoInteractor.DebugInfo(
    GetDebugInfoInteractor.DebugInfo.App(
        name = "App name",
        versionName = "versionName",
        versionCode = 123L,
        packageName = "packageName",
    ),
    GetDebugInfoInteractor.DebugInfo.Device(
        manufacturer = Build.MANUFACTURER,
        model = Build.MODEL,
        resolutionPx = "resolutionPx",
        resolutionDp = "resolutionDp",
        density = 4f,
        scaledDensity = 4f,
        densityDpi = 640,
        apiLevel = Build.VERSION.SDK_INT,
    ),
)

@DevicePreviews
@Composable
private fun PreviewDebugScreenInfo() {
    PreviewFeature {
        DebugScreen(State(previewDebugInfo, emptyList(), Environment.STAGE, selectedNavigationItem = Info), {})
    }
}

@DevicePreviews
@Composable
private fun PreviewDebugScreenOptions() {
    PreviewFeature {
        DebugScreen(State(previewDebugInfo, emptyList(), Environment.STAGE, selectedNavigationItem = Options), {})
    }
}

@DevicePreviews
@Composable
private fun PreviewDebugScreenFeatures() {
    PreviewFeature {
        DebugScreen(State(previewDebugInfo, emptyList(), Environment.STAGE, selectedNavigationItem = Features), {})
    }
}
