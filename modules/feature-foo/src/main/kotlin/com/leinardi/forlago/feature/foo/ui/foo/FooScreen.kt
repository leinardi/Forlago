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

package com.leinardi.forlago.feature.foo.ui.foo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.forlago.core.ui.component.LocalSnackbarHostState
import com.leinardi.forlago.core.ui.component.ProgressButton
import com.leinardi.forlago.core.ui.component.TopAppBar
import com.leinardi.forlago.feature.foo.R
import com.leinardi.forlago.feature.foo.ui.foo.FooContract.Effect
import com.leinardi.forlago.feature.foo.ui.foo.FooContract.Event
import com.leinardi.forlago.feature.foo.ui.foo.FooContract.State
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun FooScreen(viewModel: FooViewModel = hiltViewModel()) {
    FooScreen(
        state = viewModel.viewState.value,
        sendEvent = { viewModel.onUiEvent(it) },
        effectFlow = viewModel.effect,
    )
}

@Composable
fun FooScreen(
    state: State,
    effectFlow: Flow<Effect>,
    sendEvent: (event: Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = LocalSnackbarHostState.current
    LaunchedEffect(effectFlow) {
        effectFlow.onEach { effect ->
            when (effect) {
                is Effect.ShowSnackbar -> snackbarHostState.showSnackbar(
                    message = effect.message,
                    duration = SnackbarDuration.Short,
                )
            }
        }.collect()
    }
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(state.text)) }
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = stringResource(R.string.i18n_foo_screen_title)) },
        content = { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        top = scaffoldPadding.calculateTopPadding() + 16.dp,
                        end = 16.dp,
                        bottom = scaffoldPadding.calculateBottomPadding() + 16.dp,
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.i18n_foo_text_field_hint)) },
                )
                ProgressButton(
                    onClick = { sendEvent(Event.OnBarButtonClicked(textFieldValue.text)) },
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.i18n_foo_send_text_to_bar))
                }
                Button(
                    onClick = { sendEvent(Event.OnShowSnackbarButtonClicked) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.i18n_foo_show_snackbar))
                }
                Button(
                    onClick = { sendEvent(Event.OnShowMoreFooButtonClicked) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.i18n_foo_show_dialog))
                }
            }
        },
    )
}

@Preview
@Composable
fun PreviewFooScreen() {
    FooScreen(State("Preview", true), Channel<Effect>().receiveAsFlow(), {})
}
