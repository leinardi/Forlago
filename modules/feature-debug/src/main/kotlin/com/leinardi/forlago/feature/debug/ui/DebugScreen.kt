/*
 * Copyright 2022 Roberto Leinardi.
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

package com.leinardi.forlago.feature.debug.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.leinardi.forlago.feature.debug.R
import com.leinardi.forlago.feature.debug.interactor.GetDebugInfoInteractor
import com.leinardi.forlago.feature.debug.ui.DebugContract.Event
import com.leinardi.forlago.feature.debug.ui.DebugContract.State
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugBottomNavigationItem.Features
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugBottomNavigationItem.Info
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugBottomNavigationItem.Options
import com.leinardi.forlago.library.preferences.interactor.ReadEnvironmentInteractor
import com.leinardi.forlago.library.ui.component.ScrollableTabRow
import com.leinardi.forlago.library.ui.component.SettingsGroup
import com.leinardi.forlago.library.ui.component.SettingsMenuLink
import com.leinardi.forlago.library.ui.component.TopAppBar
import com.leinardi.forlago.library.ui.component.pagerTabIndicatorOffset
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.library.ui.theme.Spacing
import kotlinx.coroutines.launch

@Composable
fun DebugScreen(viewModel: DebugViewModel = hiltViewModel()) {
    LaunchedEffect(viewModel) {
        viewModel.onUiEvent(Event.OnViewAttached)
    }
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(R.string.debug_screen),
                onNavigateUp = { sendEvent(Event.OnUpButtonClicked) },
                scrollBehavior = if (state.selectedNavigationItem == Features) null else scrollBehavior,
            )
        },
        bottomBar = {
            NavigationBar {
                state.bottomNavigationItems.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, screen.label) },
                        label = { Text(screen.label) },
                        selected = state.selectedNavigationItem == state.bottomNavigationItems[index],
                        onClick = {
                            sendEvent(Event.OnBottomNavigationItemSelected(state.bottomNavigationItems[index]))
                        },
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
                    .padding(
                        top = scaffoldPadding.calculateTopPadding(),
                        bottom = scaffoldPadding.calculateBottomPadding(),
                    ),
            )
            Options -> Options(
                state = state,
                sendEvent = sendEvent,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize()
                    .padding(
                        start = 0.dp,
                        end = 0.dp,
                        bottom = scaffoldPadding.calculateBottomPadding(),
                        top = scaffoldPadding.calculateTopPadding(),
                    ),
            )
            Features -> Features(
                state = state,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize()
                    .padding(
                        start = 0.dp,
                        end = 0.dp,
                        bottom = scaffoldPadding.calculateBottomPadding(),
                        top = scaffoldPadding.calculateTopPadding(),
                    ),
            )
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
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier,
    ) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
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
            count = state.featureList.size,
            state = pagerState,
        ) { page ->
            state.featureList[page].composable()
        }
    }
}

@Preview
@Composable
private fun PreviewDebugScreen() {
    ForlagoTheme {
        val debugInfo = GetDebugInfoInteractor.DebugInfo(
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
        DebugScreen(State(debugInfo, emptyList(), ReadEnvironmentInteractor.DEFAULT_ENVIRONMENT), {})
    }
}
