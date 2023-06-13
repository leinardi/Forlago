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

package com.leinardi.forlago.feature.bar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.forlago.feature.bar.ui.BarContract.Event
import com.leinardi.forlago.feature.bar.ui.BarContract.State
import com.leinardi.forlago.library.ui.annotation.DevicePreviews
import com.leinardi.forlago.library.ui.component.LocalMainScaffoldPadding
import com.leinardi.forlago.library.ui.component.PreviewFeature
import com.leinardi.forlago.library.ui.component.Scaffold
import com.leinardi.forlago.library.ui.component.TopAppBar
import com.leinardi.forlago.library.ui.theme.Spacing

@Composable
fun BarScreen(viewModel: BarViewModel = hiltViewModel()) {
    BarScreen(
        state = viewModel.viewState.value,
        sendEvent = { viewModel.onUiEvent(it) },
    )
}

@Composable
private fun BarScreen(
    state: State,
    sendEvent: (event: Event) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(LocalMainScaffoldPadding.current.value)
            .consumeWindowInsets(LocalMainScaffoldPadding.current.value)
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_bar_screen_title),
                onNavigateUp = { sendEvent(Event.OnUpButtonClicked) },
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                scrollBehavior = scrollBehavior,
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = scaffoldPadding.calculateStartPadding(LocalLayoutDirection.current) + Spacing.x02,
                    top = scaffoldPadding.calculateTopPadding() + Spacing.x02,
                    end = scaffoldPadding.calculateEndPadding(LocalLayoutDirection.current) + Spacing.x02,
                    bottom = scaffoldPadding.calculateBottomPadding() + Spacing.x02,
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_bar_text_received, state.text),
                    Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = typography.headlineSmall,
                )
            }
            Button(
                onClick = { sendEvent(Event.OnBackButtonClicked) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_back))
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewBarScreen() {
    PreviewFeature {
        BarScreen(State("Preview")) {}
    }
}
