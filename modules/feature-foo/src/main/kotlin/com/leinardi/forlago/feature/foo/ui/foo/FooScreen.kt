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

package com.leinardi.forlago.feature.foo.ui.foo

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.forlago.feature.foo.ui.foo.FooContract.Effect
import com.leinardi.forlago.feature.foo.ui.foo.FooContract.Event
import com.leinardi.forlago.feature.foo.ui.foo.FooContract.State
import com.leinardi.forlago.library.ui.annotation.DevicePreviews
import com.leinardi.forlago.library.ui.component.LocalMainScaffoldPadding
import com.leinardi.forlago.library.ui.component.PreviewFeature
import com.leinardi.forlago.library.ui.component.ProgressButton
import com.leinardi.forlago.library.ui.component.Scaffold
import com.leinardi.forlago.library.ui.component.TopAppBar
import com.leinardi.forlago.library.ui.theme.Spacing
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun FooScreen(viewModel: FooViewModel = hiltViewModel()) {
    FooScreen(
        state = viewModel.viewState.value,
        sendEvent = { viewModel.onUiEvent(it) },
        effectFlow = viewModel.effect,
    )
}

@Composable
private fun FooScreen(
    state: State,
    effectFlow: Flow<Effect>,
    sendEvent: (event: Event) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(effectFlow) {
        effectFlow.onEach { effect ->
            when (effect) {
                is Effect.ShowSnackbar -> launch {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }.collect()
    }
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(state.text)) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(LocalMainScaffoldPadding.current.value)
            .consumeWindowInsets(LocalMainScaffoldPadding.current.value)
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_foo_screen_title),
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_foo_text_field_hint)) },
            )
            ProgressButton(
                onClick = { sendEvent(Event.OnBarButtonClicked(textFieldValue.text)) },
                loading = state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_foo_send_text_to_bar))
            }
            Button(
                onClick = { sendEvent(Event.OnShowSnackbarButtonClicked) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_foo_show_snackbar))
            }
            Button(
                onClick = { sendEvent(Event.OnShowMoreFooButtonClicked) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_foo_show_dialog))
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewFooScreen() {
    PreviewFeature {
        FooScreen(State("Preview", true), Channel<Effect>().receiveAsFlow()) {}
    }
}
