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

package com.leinardi.forlago.feature.account.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.forlago.core.ui.component.OutlinedTextField
import com.leinardi.forlago.core.ui.component.ProgressButton
import com.leinardi.forlago.core.ui.component.TopAppBar
import com.leinardi.forlago.core.ui.theme.ForlagoTheme
import com.leinardi.forlago.feature.account.ui.SignInContract.Effect
import com.leinardi.forlago.feature.account.ui.SignInContract.Event
import com.leinardi.forlago.feature.account.ui.SignInContract.State
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun SignInScreen() {
    val viewModel = hiltViewModel<SignInViewModel>()

    SignInScreen(
        state = viewModel.viewState.value,
        sendEvent = { viewModel.onUiEvent(it) },
        effectFlow = viewModel.effect,
    )
}

@Composable
fun SignInScreen(
    state: State,
    effectFlow: Flow<Effect>,
    sendEvent: (event: Event) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(effectFlow) {
        effectFlow.onEach { effect ->
            when (effect) {
                is Effect.ShowErrorSnackbar -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Indefinite,
                        actionLabel = effect.actionLabel,
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
            }
        }.collect()
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = "Account screen") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val localFocusManager = LocalFocusManager.current
                var username by rememberSaveable { mutableStateOf(state.username) }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isReauthenticate,
                    value = username,
                    onValueChange = { username = it },
                    label = "Username",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { localFocusManager.moveFocus(FocusDirection.Down) },
                    ),
                )
                var password by rememberSaveable { mutableStateOf(state.password) }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    passwordToggleEnabled = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            localFocusManager.clearFocus()
                            sendEvent(Event.OnSignInButtonClicked(username, password))
                        },
                    ),
                )
                ProgressButton(
                    onClick = {
                        localFocusManager.clearFocus()
                        sendEvent(Event.OnSignInButtonClicked(username, password))
                    },
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Sign in")
                }
            }
        },
    )
}

@Preview
@Composable
fun PreviewAccountScreen() {
    ForlagoTheme {
        SignInScreen(State(false, "", ""), Channel<Effect>().receiveAsFlow()) {}
    }
}
