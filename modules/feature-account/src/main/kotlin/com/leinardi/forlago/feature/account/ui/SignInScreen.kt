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

package com.leinardi.forlago.feature.account.ui

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.forlago.feature.account.ui.SignInContract.Effect
import com.leinardi.forlago.feature.account.ui.SignInContract.Event
import com.leinardi.forlago.feature.account.ui.SignInContract.State
import com.leinardi.forlago.library.ui.component.LocalMainScaffoldPadding
import com.leinardi.forlago.library.ui.component.OutlinedTextField
import com.leinardi.forlago.library.ui.component.PreviewFeature
import com.leinardi.forlago.library.ui.component.ProgressButton
import com.leinardi.forlago.library.ui.component.Scaffold
import com.leinardi.forlago.library.ui.component.TopAppBar
import com.leinardi.forlago.library.ui.preview.PreviewDevices
import com.leinardi.forlago.library.ui.theme.Spacing
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(viewModel: SignInViewModel = hiltViewModel()) {
    SignInScreen(
        state = viewModel.viewState.value,
        sendEvent = { viewModel.onUiEvent(it) },
        effectFlow = viewModel.effect,
    )
}

@Composable
private fun SignInScreen(
    state: State,
    effectFlow: Flow<Effect>,
    sendEvent: (event: Event) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(effectFlow) {
        effectFlow.onEach { effect ->
            when (effect) {
                is Effect.ShowErrorSnackbar -> launch {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Indefinite,
                        actionLabel = effect.actionLabel,
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
            }
        }.collect()
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(LocalMainScaffoldPadding.current.value)
            .consumeWindowInsets(LocalMainScaffoldPadding.current.value)
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = "Account screen",
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = scaffoldPadding.calculateStartPadding(LocalLayoutDirection.current) + Spacing.x02,
                    top = scaffoldPadding.calculateTopPadding() + Spacing.x02,
                    end = scaffoldPadding.calculateEndPadding(LocalLayoutDirection.current) + Spacing.x02,
                    bottom = scaffoldPadding.calculateBottomPadding() + Spacing.x02,
                ),
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
    }
}

@PreviewDevices
@Composable
private fun PreviewAccountScreen() {
    PreviewFeature {
        SignInScreen(State(false, "", ""), Channel<Effect>().receiveAsFlow()) {}
    }
}
