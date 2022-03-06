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
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.leinardi.forlago.core.ui.component.BottomNavigation
import com.leinardi.forlago.core.ui.component.LocalSnackbarHostState
import com.leinardi.forlago.core.ui.component.ScrollableTabRow
import com.leinardi.forlago.core.ui.component.SettingsGroup
import com.leinardi.forlago.core.ui.component.SettingsMenuLink
import com.leinardi.forlago.core.ui.component.TopAppBar
import com.leinardi.forlago.core.ui.theme.ForlagoTypography
import com.leinardi.forlago.feature.debug.R
import com.leinardi.forlago.feature.debug.interactor.GetDebugInfoInteractor
import com.leinardi.forlago.feature.debug.ui.DebugContract.Event
import com.leinardi.forlago.feature.debug.ui.DebugContract.State
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugBottomNavigationItem.Features
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugBottomNavigationItem.Info
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugBottomNavigationItem.Options
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

@Composable
fun DebugScreen(
    state: State,
    sendEvent: (event: Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldState = rememberScaffoldState()
    CompositionLocalProvider(
        LocalSnackbarHostState provides scaffoldState.snackbarHostState,
    ) {
        Scaffold(
            modifier = modifier,
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = stringResource(R.string.debug_screen),
                    onNavigateUp = { sendEvent(Event.OnUpButtonClicked) },
                    elevation = if (state.selectedNavigationItem == Features) 0.dp else AppBarDefaults.TopAppBarElevation,
                )
            },
            content = { innerPadding ->
                when (state.selectedNavigationItem) {
                    Info -> Info(
                        state = state,
                        modifier = modifier
                            .background(MaterialTheme.colors.surface)
                            .fillMaxSize()
                            .padding(
                                start = 0.dp,
                                end = 0.dp,
                                bottom = innerPadding.calculateBottomPadding(),
                                top = innerPadding.calculateTopPadding(),
                            ),
                    )
                    Options -> Options(
                        sendEvent = sendEvent,
                        modifier = modifier
                            .background(MaterialTheme.colors.surface)
                            .fillMaxSize()
                            .padding(
                                start = 0.dp,
                                end = 0.dp,
                                bottom = innerPadding.calculateBottomPadding(),
                                top = innerPadding.calculateTopPadding(),
                            ),
                    )
                    Features -> Features(
                        state = state,
                        modifier = modifier
                            .background(MaterialTheme.colors.surface)
                            .fillMaxSize()
                            .padding(
                                start = 0.dp,
                                end = 0.dp,
                                bottom = innerPadding.calculateBottomPadding(),
                                top = innerPadding.calculateTopPadding(),
                            ),
                    )
                }
            },
            bottomBar = {
                BottomNavigation {
                    state.bottomNavigationItems.forEachIndexed { index, screen ->
                        BottomNavigationItem(
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
        )
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
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = state.debugInfo.app.name,
                color = MaterialTheme.colors.primary,
                style = ForlagoTypography.h4,
            )
            ProvideTextStyle(value = MaterialTheme.typography.caption) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.medium,
                    content = {
                        Text(text = "Version name: ${state.debugInfo.app.versionName}")
                        Text(text = "Version code: ${state.debugInfo.app.versionCode}")
                        Text(text = "Application ID: ${state.debugInfo.app.packageName}")
                    },
                )
            }
        }
        DeviceInfo(state)
    }
}

@Composable
private fun Options(
    sendEvent: (event: Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        SettingsGroup(
            title = { Text(text = "GraphQL") },
        ) {
            Button(
                onClick = { sendEvent(Event.OnClearApolloCacheClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text("Clear Apollo cache")
            }
        }
        SettingsGroup(
            title = { Text(text = "Crashlytics") },
        ) {
            Button(
                onClick = { sendEvent(Event.OnForceCrashClicked) },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
fun PreviewDebugScreen() {
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
    DebugScreen(State(debugInfo, emptyList()), {})
}
